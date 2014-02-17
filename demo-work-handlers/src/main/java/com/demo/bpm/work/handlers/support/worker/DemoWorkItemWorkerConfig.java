package com.demo.bpm.work.handlers.support.worker;

import java.io.File;


import com.demo.bpm.facts.model.WorkDay;
import com.demo.bpm.work.handlers.support.fact.KieSessionDemoFactManager;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

public class DemoWorkItemWorkerConfig
{
    public static final String IN_WORK_ID = "WorkId";
    public static final String IN_SIGNAL_ON_ERROR = "SignalOnError";
    protected final WorkItem workItem;
    protected final File logBaseDir;
    protected File workItemLogDir;
    protected Long workItemId;
    protected String workDoneId;
    protected boolean signalOnErrorResult;

    public DemoWorkItemWorkerConfig(WorkItem workItem, File logBaseDir)
    {
        this.workItem = workItem;
        this.workItemId = workItem.getId();
        this.logBaseDir = logBaseDir;
    }

    public void init(KieSession kieSession)
    {
        this.signalOnErrorResult = getBooleanParameter(IN_SIGNAL_ON_ERROR, true);
        this.workDoneId = extractWorkDoneIdFromParameters();

        // derive a working log directory based on the Work Day and the current process
        ProcessInstance process = kieSession.getProcessInstance(workItem.getProcessInstanceId());
        WorkDay workDay = new KieSessionDemoFactManager(kieSession).find(WorkDay.class);

        File workItemLogBaseDir = workDay == null ? logBaseDir : new File(logBaseDir, workDay.getName());
        workItemLogBaseDir = new File(workItemLogBaseDir, process.getProcessName());
        this.workItemLogDir = new File(workItemLogBaseDir, workDoneId);
        this.workItemLogDir.mkdirs();
    }

    public boolean isSignalOnErrorResult()
    {
        return signalOnErrorResult;
    }

    public void setSignalOnErrorResult(boolean signalOnErrorResult)
    {
        this.signalOnErrorResult = signalOnErrorResult;
    }

    public String getWorkDoneId()
    {
        return workDoneId;
    }

    public void setWorkDoneId(String workDoneId)
    {
        this.workDoneId = workDoneId;
    }

    public File getWorkItemLogDir()
    {
        return workItemLogDir;
    }

    public void setWorkItemLogDir(File workItemLogDir)
    {
        this.workItemLogDir = workItemLogDir;
    }

    public Long getWorkItemId()
    {
        return workItemId;
    }

    public void setWorkItemId(Long workItemId)
    {
        this.workItemId = workItemId;
    }

    public WorkItem getWorkItem()
    {
        return workItem;
    }

    protected String extractWorkDoneIdFromParameters()
    {
        return getStringParameter(IN_WORK_ID, this.workItemId.toString());
    }

    protected Object getObjectParameter(String name, Object def)
    {
        Object val = def;
        if (workItem.getParameter(name) != null && ((String) workItem.getParameter(name)).length() > 0)
            return workItem.getParameter(name);
        else
            return val;
    }

    protected boolean getBooleanParameter(String name, boolean def)
    {
        boolean val = def;
        if (workItem.getParameter(name) != null && ((String) workItem.getParameter(name)).length() > 0)
            return Boolean.valueOf((String) workItem.getParameter(name));
        else
            return val;
    }

    protected String getStringParameter(String name, String def)
    {
        String val = def;
        if (workItem.getParameter(name) != null && ((String) workItem.getParameter(name)).length() > 0)
            return (String) workItem.getParameter(name);
        else
            return val;
    }

    protected RuleFlowProcessInstance getProcessInstance(KieSession kieSession)
    {
        return (RuleFlowProcessInstance) kieSession.getProcessInstance(workItem.getProcessInstanceId());
    }


}
