package com.demo.bpm.work.handlers.support.worker;

import org.apache.log4j.Logger;
import org.jbpm.process.workitem.AbstractWorkItemHandler;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
import org.jbpm.workflow.core.node.BoundaryEventNode;
import org.kie.api.definition.process.Node;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.internal.runtime.StatefulKnowledgeSession;

public class BpmnSignalThrower
{
    private static final Logger logger = Logger.getLogger(BpmnSignalThrower.class);
    private final String errorSignalName;
    private final NodeInstance workItemNodeInstance;
    private final long processInstanceId;
    private final StatefulKnowledgeSession ksession;
    private String boundaryNodeTypedErrorSignalName;

    public BpmnSignalThrower(AbstractWorkItemHandler myHandler, WorkItem wi, String errorSignalName)
    {
        this.errorSignalName = errorSignalName;
        this.workItemNodeInstance = myHandler.getNodeInstance(wi);
        this.processInstanceId = wi.getProcessInstanceId();
        this.ksession = myHandler.getSession();
        // I used to do this lazily, but if I don't do it now I risk pulling the process instance into
        // another thread (and transaction).
        this.boundaryNodeTypedErrorSignalName = formBoundaryErrorSignalName();
    }

    public void signalEvent(Object data)
    {
        logger.warn("Sending signal " + errorSignalName + " with data " + data.toString());

        // the boundary event node signal must be designed so that any boundary event node listening will hear
        ksession.signalEvent(boundaryNodeTypedErrorSignalName, data, processInstanceId);

        // also signal generically for any process event node (non-boundary)
        ksession.signalEvent(errorSignalName, data, processInstanceId);

        // finally signal out of process in case anyone is listening
        ksession.signalEvent(errorSignalName, data);

    }

    // the error signal for the targeted boundary event is custom for any boundary event attached to this node. It is
    // formed by including the Boundary Event node to this work item as part of the error signal name.
    //
    // There is no node pointer from the Work Item node to its boundary event nodes (if any).  I must visit every node
    // in the process diagram and try and find BoundaryEventNodes that point back to my work item.
    //
    // note that the code as written only supports a single boundary event node.
    private String formBoundaryErrorSignalName()
    {
        Node[] bpmnNodes = ((WorkflowProcessImpl) (workItemNodeInstance.getProcessInstance()).getProcess()).getNodes();
        String workItemNodeId = (String) workItemNodeInstance.getNode().getMetaData().get("UniqueId");
        for (Node n : bpmnNodes)
        {
            if (!(n instanceof BoundaryEventNode))
                continue;
            BoundaryEventNode ben = (BoundaryEventNode) n;
            if (ben.getAttachedToNodeId().equals(workItemNodeId))
                return "Error-" + ben.getAttachedToNodeId() + "-" + errorSignalName;
        }
        return "Error-" + errorSignalName;
    }

}
