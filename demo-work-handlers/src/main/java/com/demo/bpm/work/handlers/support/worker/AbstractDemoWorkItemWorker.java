package com.demo.bpm.work.handlers.support.worker;

import com.demo.bpm.work.handlers.support.DemoWorkItemHandlerResults;
import com.demo.bpm.work.handlers.support.fact.KieSessionDemoFactManager;
import org.apache.log4j.Logger;
import org.kie.api.runtime.KieSession;

public abstract class AbstractDemoWorkItemWorker
        implements Runnable
{
    private static final Logger log = Logger.getLogger(AbstractDemoWorkItemWorker.class);
    private final DemoWorkItemWorkerConfig config;
    protected KieSession kieSession;
    private BpmnSignalThrower signalThrower;
    private KieSessionDemoFactManager factManager;

    public AbstractDemoWorkItemWorker(DemoWorkItemWorkerConfig config)
    {
        this.config = config;
    }

    public abstract DemoWorkItemHandlerResults doWorkInThread()
            throws InterruptedException;

    public void run()
    {

        DemoWorkItemHandlerResults rr;

        try
        {
            rr = doWorkInThread();
        } catch (InterruptedException e)
        {
            log.warn("Work item thread was killed for : " + config.getWorkItem());
            //quietly exit, someone killed us intentionally
            return;
        } catch (Exception e)
        {
            // signal an exception occurred to anyone who may be listening
            signalThrower.signalEvent(e);
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            else
                throw new RuntimeException(e);
        }

        // if the work task has been configured to signal if there was an error result then do it now
        if (rr.getReturnCode() != 0 && config.isSignalOnErrorResult())
            signalThrower.signalEvent(config.getWorkDoneId());

        // notify manager that work item has been completed.  We cannot keep a handle to
        // the WorkItemManager around - the transaction it is within will have been
        // closed by the time we get here.  Instead get it directly from the session when
        // we need it
        kieSession.getWorkItemManager().completeWorkItem(config.getWorkItemId(), rr.getResults());

    }

    public void setFactManager(KieSessionDemoFactManager factManager)
    {
        this.factManager = factManager;
    }

    public void setKieSession(KieSession kieSession)
    {
        this.kieSession = kieSession;
    }

    public BpmnSignalThrower getSignalThrower()
    {
        return signalThrower;
    }

    public void setSignalThrower(BpmnSignalThrower signalThrower)
    {
        this.signalThrower = signalThrower;
    }

    public DemoWorkItemWorkerConfig getConfig()
    {
        return config;
    }

    public KieSessionDemoFactManager getFactManager()
    {
        return factManager;
    }
}
