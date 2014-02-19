package com.demo.bpm.work.handlers.implementations.simple.worker;

import com.demo.bpm.facts.model.WorkDone;
import com.demo.bpm.work.handlers.support.DemoWorkItemHandlerResults;
import com.demo.bpm.work.handlers.support.worker.AbstractDemoWorkItemWorker;
import com.demo.bpm.work.handlers.support.worker.DemoWorkItemWorkerConfig;

public class SimpleDemoWorkItemWorker
    extends AbstractDemoWorkItemWorker
{
    public SimpleDemoWorkItemWorker(DemoWorkItemWorkerConfig config)
    {
        super(config);
    }

    @Override
    public DemoWorkItemHandlerResults doWorkInThread()
            throws InterruptedException
    {
        System.out.println("##########");
        // this is where you do real work that takes a long time
        Thread.sleep(20000L);
        System.out.println("@@@@@@@@@@");
        // update the current agenda with results from all our work
        getFactManager().add(new WorkDone("We did some work on thread " + Thread.currentThread().getName() ));

        // you can make your own results description, here I just return OK
        return DemoWorkItemHandlerResults.OK_RESULTS;
    }
}
