package com.demo.bpm.jee.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeployedProcessDesc
{
    private String processId;
    @XmlElementWrapper(name = "executions")
    @XmlElement(name = "run")
    private List<DeployedProcessActivity> activities = new ArrayList<DeployedProcessActivity>();
    @XmlTransient
    private DeployedWorkDayDesc deployedWorkDay;

    public DeployedProcessDesc()
    {
    }

    public DeployedProcessDesc(String processId)
    {
        this.processId = processId;
    }

    public void addActivity(DeployedProcessActivity activity)
    {
        activity.setProcess(this);
        activities.add(activity);
    }

    // reset the transient marked by @XmlTransient
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        this.deployedWorkDay = (DeployedWorkDayDesc)parent;
    }

    public String getProcessId()
    {
        return processId;
    }

    public List<DeployedProcessActivity> getActivities()
    {
        return activities;
    }

    public DeployedWorkDayDesc getDeployedWorkDay()
    {
        return deployedWorkDay;
    }

    public void setDeployedWorkDay(DeployedWorkDayDesc deployedWorkDay)
    {
        this.deployedWorkDay = deployedWorkDay;
    }
}
