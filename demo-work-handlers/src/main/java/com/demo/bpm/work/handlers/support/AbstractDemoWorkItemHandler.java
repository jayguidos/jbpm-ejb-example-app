package com.demo.bpm.work.handlers.support;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;


import com.demo.bpm.work.handlers.support.fact.KieSessionDemoFactManager;
import com.demo.bpm.work.handlers.support.worker.AbstractDemoWorkItemWorker;
import com.demo.bpm.work.handlers.support.worker.BpmnSignalThrower;
import org.apache.log4j.Logger;
import org.jbpm.process.workitem.AbstractWorkItemHandler;
import org.jbpm.runtime.manager.impl.SingletonRuntimeManager;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.internal.runtime.manager.context.EmptyContext;

public abstract class AbstractDemoWorkItemHandler
        extends AbstractWorkItemHandler
{
    public static final String THREAD_NAME_PREFIX = "DemoWorkItemWorkerThread-";
    private static final Logger log = Logger.getLogger(AbstractDemoWorkItemHandler.class);
    private static int threadCounter = 1;
    private final String errorSignalName;
    private File logBaseDir;
    private AtomicReference<Thread> workerThread = new AtomicReference<Thread>();
    private AtomicReference<AbstractDemoWorkItemWorker> worker = new AtomicReference<AbstractDemoWorkItemWorker>();
    private SingletonRuntimeManager runtimeManager;

    protected AbstractDemoWorkItemHandler(KieSession kieSession, String errorSignalName)
    {
        super((StatefulKnowledgeSession) kieSession);
        this.errorSignalName = errorSignalName;
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager)
    {
        AbstractDemoWorkItemWorker w = worker.get();
        if (w != null)
            log.warn("Aborting work item: " + w.getConfig().getWorkItem());
        Thread t = workerThread.getAndSet(null);
        if (t != null)
        {
            t.interrupt();
            try
            {
                t.join();
            } catch (InterruptedException ignored)
            {
            }
        }
        log.warn("Aborted work item: " + w.getConfig().getWorkItem());
    }

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager)
    {
        BpmnSignalThrower signalThrower = new BpmnSignalThrower(this, workItem, errorSignalName);

        // be sure to catch exceptions on this thread if something happens while I am building the worker
        try
        {
            AbstractDemoWorkItemWorker w = makeWorkItemWorker(workItem);
            w.setSignalThrower(signalThrower);
            w.setKieSession(getSession());
            this.worker.set(w);
        } catch (Exception e)
        {
            signalThrower.signalEvent(e);
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            else
                throw new RuntimeException(e);
        }

        // exceptions on the worker thread will be caught by instances of AbstractDemoWorkItemWorker,
        // and InterruptedExceptions are allowed to propagate
        workerThread.set(new Thread(new WorkerRunner(), THREAD_NAME_PREFIX + (threadCounter++)));
        workerThread.get().start();
    }

    public KieSession getKsession()
    {
        // presumes a global singleton engine for this runtime, otherwise I would have to provide
        // something in the context
        return runtimeManager.getRuntimeEngine(EmptyContext.get()).getKieSession();
    }

    public RuntimeManager getRuntimeManager()
    {
        return runtimeManager;
    }

    public void setRuntimeManager(SingletonRuntimeManager runtimeManager)
    {
        this.runtimeManager = runtimeManager;
    }

    public File getLogBaseDir()
    {
        return logBaseDir;
    }

    public void setLogBaseDir(File logBaseDir)
    {
        this.logBaseDir = logBaseDir;
    }

    protected abstract AbstractDemoWorkItemWorker makeWorkItemWorker(WorkItem workItem);

    private class WorkerRunner
            implements Runnable
    {
        public void run()
        {
            AbstractDemoWorkItemWorker w = null;
            try
            {
                w = worker.get();
                w.setFactManager(new KieSessionDemoFactManager(getKsession()));
                if (w != null)
                    w.run();
            } finally
            {
                worker.set(null);
                workerThread.set(null);
                if (w != null)
                    w.setFactManager(null);
            }
        }
    }
}
