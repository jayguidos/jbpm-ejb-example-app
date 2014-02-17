package com.demo.bpm.work.handlers.implementations.simple;

import com.demo.bpm.work.handlers.implementations.simple.worker.SimpleDemoWorkItemWorker;
import com.demo.bpm.work.handlers.support.AbstractDemoWorkItemHandler;
import com.demo.bpm.work.handlers.support.worker.AbstractDemoWorkItemWorker;
import com.demo.bpm.work.handlers.support.worker.DemoWorkItemWorkerConfig;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;

public class SimpleDemoWorkItemHandlerImpl
        extends AbstractDemoWorkItemHandler
{
    protected SimpleDemoWorkItemHandlerImpl(KieSession kieSession, String errorSignalName)
    {
        super(kieSession, errorSignalName);
    }

    @Override
    protected AbstractDemoWorkItemWorker makeWorkItemWorker(WorkItem workItem)
    {
        DemoWorkItemWorkerConfig config = new DemoWorkItemWorkerConfig(workItem, getLogBaseDir());
        config.init(getKsession());
        return new SimpleDemoWorkItemWorker(config);
    }
}
