package com.demo.bpm.work.handlers.implementations.simple;

import java.io.File;


import com.demo.bpm.work.handlers.support.AbstractDemoWorkItemHandlerFactoryImpl;
import org.jbpm.runtime.manager.impl.SingletonRuntimeManager;

public class SimpleDemoWorkItemHandlerFactoryImpl
        extends AbstractDemoWorkItemHandlerFactoryImpl<SimpleDemoWorkItemHandlerImpl>
{
    public SimpleDemoWorkItemHandlerFactoryImpl(SingletonRuntimeManager runtimeManager, File logBaseDir)
    {
        super(runtimeManager, logBaseDir);
    }

    public SimpleDemoWorkItemHandlerImpl makeWorkItem()
    {
        return configureItem(new SimpleDemoWorkItemHandlerImpl(getKieSession(),"simpleSignal"));
    }
}
