package com.demo.bpm.jee.kie;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import com.demo.bpm.jee.model.DemoDeployment;
import com.demo.bpm.jee.model.DeployedProcessActivity;
import com.demo.bpm.jee.model.DeployedProcessDesc;
import com.demo.bpm.jee.model.DeployedProcessNodeActivity;
import com.demo.bpm.jee.model.DeployedWorkDayDesc;
import org.jbpm.kie.services.api.RuntimeDataService;
import org.jbpm.kie.services.impl.model.NodeInstanceDesc;
import org.jbpm.kie.services.impl.model.ProcessAssetDesc;
import org.jbpm.kie.services.impl.model.ProcessInstanceDesc;

public class WorkDayActivityReporter
{

    private final RuntimeDataService rds;
    private final boolean withHistory;
    private final List<Integer> allStates = DeployedProcessActivity.State.asIntegers();

    public WorkDayActivityReporter(RuntimeDataService rds, boolean withHistory)
    {
        this.rds = rds;
        this.withHistory = withHistory;
    }

    public DeployedWorkDayDesc reportDeploymentActivity(DemoDeployment dd)
    {
        DeployedWorkDayDesc dwd = new DeployedWorkDayDesc(dd);
        addAllProcessAssets(dwd, rds.getProcessesByDeploymentId(dd.getDeployIdentifier()));
        return dwd;
    }

    public DeployedWorkDayDesc reportProcessActivity(DemoDeployment dd, String pid)
    {
        DeployedWorkDayDesc dwd = new DeployedWorkDayDesc(dd);
        ProcessAssetDesc pad = rds.getProcessesByDeploymentIdProcessId(dd.getDeployIdentifier(), pid);
        if (pad != null)
            addAllProcessAssets(dwd, Arrays.asList(pad));
        return dwd;
    }

    protected void addAllProcessAssets(DeployedWorkDayDesc dwd, Collection<ProcessAssetDesc> processes)
    {
        for (ProcessAssetDesc pad : processes)
            addProcessAssets(dwd, pad);
    }

    protected void addProcessAssets(DeployedWorkDayDesc dwd, ProcessAssetDesc p)
    {
        DeployedProcessDesc dp = new DeployedProcessDesc(p.getId());
        dwd.addProcess(dp);
        // I want by deployment ID and process ID, but there is no API for both filters.  So get them
        // all and filter myself
        for (ProcessInstanceDesc pid : rds.getProcessInstancesByProcessId(allStates, dp.getProcessId(), null))
            if (dp.getDeployedWorkDay().getDeploymentId().equals(pid.getDeploymentId()))
                addProcessInstance(dp, pid);
    }

    private void addProcessInstance(DeployedProcessDesc dp, ProcessInstanceDesc pid)
    {
        DeployedProcessActivity dpa = new DeployedProcessActivity(pid.getId(), pid.getState(), pid.getDataTimeStamp());
        String deploymentId = dp.getDeployedWorkDay().getDeploymentId();
        Collection<NodeInstanceDesc> nodeHistory;
        if (withHistory)
            nodeHistory = rds.getProcessInstanceFullHistory(deploymentId, pid.getId());
        else
            nodeHistory = rds.getProcessInstanceActiveNodes(deploymentId, pid.getId());
        for (NodeInstanceDesc nid : nodeHistory)
            dpa.add(new DeployedProcessNodeActivity(
                    dpa,
                    nid.getId(),
                    nid.getNodeId(),
                    nid.getNodeType(),
                    nid.getName(),
                    nid.isCompleted(),
                    nid.getDataTimeStamp())
            );
        dp.addActivity(dpa);
    }
}

