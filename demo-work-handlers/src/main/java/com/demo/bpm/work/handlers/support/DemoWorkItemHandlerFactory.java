package com.demo.bpm.work.handlers.support;

public interface DemoWorkItemHandlerFactory<T extends AbstractDemoWorkItemHandler>
{
    T makeWorkItem();
}
