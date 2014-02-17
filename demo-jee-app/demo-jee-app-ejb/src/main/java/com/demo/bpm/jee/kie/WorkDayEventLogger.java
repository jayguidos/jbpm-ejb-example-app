package com.demo.bpm.jee.kie;

import java.util.logging.Logger;


import com.demo.bpm.facts.model.WorkDay;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEvent;
import org.kie.api.event.process.ProcessNodeEvent;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;

public class WorkDayEventLogger
        extends DefaultProcessEventListener
{
    Logger logger;

    public WorkDayEventLogger(WorkDay bd)
    {
        logger = Logger.getLogger(this.getClass().getName() + "." + bd.getName());
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent event)
    {
        logger.info("NodeLeft" + getProcessName(event) + " : " + getNodeName(event));
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event)
    {
        logger.info("NodeTriggered" + getProcessName(event) + " : " + getNodeName(event));
    }

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event)
    {
        logger.info("ProcessCompleted" + getProcessName(event));
    }

    @Override
    public void beforeProcessStarted(ProcessStartedEvent event)
    {
        logger.info("ProcessStarted" + getProcessName(event));
    }

    private String getProcessName(ProcessEvent event)
    {
        return "[" +event.getProcessInstance().getProcessName() + "(" + event.getProcessInstance().getId() + ")]";
    }

    protected String getNodeName(ProcessNodeEvent event)
    {
        String nodeName = event.getNodeInstance().getNodeName();
        if ( nodeName != null && nodeName.length() > 0 )
            return nodeName;
        else
            return event.getNodeInstance().getNode().getMetaData().get("UniqueId").toString();
    }
}
