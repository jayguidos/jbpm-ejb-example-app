package com.demo.bpm.work.handlers.support;

import java.io.File;


import org.jbpm.runtime.manager.impl.SingletonRuntimeManager;
import org.kie.api.runtime.KieSession;
import org.kie.internal.runtime.manager.context.EmptyContext;

public abstract class AbstractDemoWorkItemHandlerFactoryImpl<T extends AbstractDemoWorkItemHandler>
        implements DemoWorkItemHandlerFactory<T>
{

    protected final SingletonRuntimeManager runtimeManager;
    protected final File logBaseDir;

    public AbstractDemoWorkItemHandlerFactoryImpl(SingletonRuntimeManager runtimeManager, File logBaseDir)
    {
        this.runtimeManager = runtimeManager;
        this.logBaseDir = logBaseDir;
    }

    public SingletonRuntimeManager getRuntimeManager()
    {
        return runtimeManager;
    }

    protected KieSession getKieSession()
    {
        return this.runtimeManager.getRuntimeEngine(EmptyContext.get()).getKieSession();
    }

    protected T configureItem(T item)
    {
        item.setRuntimeManager(runtimeManager);
        item.setLogBaseDir(logBaseDir);
        return item;
    }
}
