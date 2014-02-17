package com.demo.bpm.jee.cdi;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


import com.demo.bpm.jee.util.DemoJBPMConfiguration;
import static com.demo.bpm.shared.DemoBPMConstants.SIMPLE_WORK_ITEM_HANDLER;
import com.demo.bpm.work.handlers.implementations.simple.SimpleDemoWorkItemHandlerFactoryImpl;
import org.jbpm.runtime.manager.api.WorkItemHandlerProducer;
import org.jbpm.runtime.manager.impl.SingletonRuntimeManager;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.task.TaskService;
import org.kie.internal.executor.api.ExecutorService;

public class SimpleDemoWorkItemProducer
        implements WorkItemHandlerProducer
{

    // these constants are defined as KIE internals
    public static final String RUNTIME_MANAGER = "runtimeManager";
    public static final String KIE_SESSION = "ksession";
    public static final String EXECUTOR_SERVICE = "executorService";
    public static final String TASK_SERVICE = "taskService";

    @Inject
    private DemoJBPMConfiguration config;

    public Map<String, WorkItemHandler> getWorkItemHandlers(String identifier, Map<String, Object> params)
    {
        HashMap<String, WorkItemHandler> wihMap = new HashMap<String, WorkItemHandler>();
        SingletonRuntimeManager runtimeManager = getRuntimeManager(params);
        SimpleDemoWorkItemHandlerFactoryImpl factory = new SimpleDemoWorkItemHandlerFactoryImpl(runtimeManager, config.getGlobalLogDir());
        wihMap.put(SIMPLE_WORK_ITEM_HANDLER, factory.makeWorkItem());
        return wihMap;
    }

    protected SingletonRuntimeManager getRuntimeManager(Map<String, Object> injectedMap)
    {
        return (SingletonRuntimeManager) injectedMap.get(RUNTIME_MANAGER);
    }

    protected KieSession getKieSession(Map<String, Object> injectedMap)
    {
        return (KieSession) injectedMap.get(KIE_SESSION);
    }

    protected ExecutorService getExecutorService(Map<String, Object> injectedMap)
    {
        return (ExecutorService) injectedMap.get(EXECUTOR_SERVICE);
    }

    protected TaskService getTaskService(Map<String, Object> injectedMap)
    {
        return (TaskService) injectedMap.get(TASK_SERVICE);
    }
}
