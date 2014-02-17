package com.demo.bpm.work.handlers.support;

import java.util.HashMap;


import com.demo.bpm.facts.model.WorkDone;

public class DemoWorkItemHandlerResults
{
    public static final String RETURN_CODE = "ReturnCode";
    public static DemoWorkItemHandlerResults ERROR_RESULTS = new DemoWorkItemHandlerResults(-1);
    public static DemoWorkItemHandlerResults OK_RESULTS = new DemoWorkItemHandlerResults(1);
    private final HashMap<String, Object> results = new HashMap<String, Object>();
    private int returnCode = -1;
    private WorkDone workDone;

    public DemoWorkItemHandlerResults(int returnCode)
    {
        setReturnCode(returnCode);
    }

    public DemoWorkItemHandlerResults(int returnCode, WorkDone workDone)
    {
        setReturnCode(returnCode);
        setWorkDone(workDone);
    }

    public DemoWorkItemHandlerResults()
    {
    }

    public void addResult(String name, Object val)
    {
        results.put(name, val);
    }

    public HashMap<String, Object> getResults()
    {
        return results;
    }

    public int getReturnCode()
    {
        return returnCode;
    }

    public void setReturnCode(int returnCode)
    {
        this.returnCode = returnCode;
        addResult(RETURN_CODE, returnCode);
    }

    public WorkDone getWorkDone()
    {
        return workDone;
    }

    public void setWorkDone(WorkDone workDone)
    {
        this.workDone = workDone;
    }
}
