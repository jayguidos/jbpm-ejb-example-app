package com.demo.bpm.jee.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


import com.demo.bpm.facts.model.WorkDay;

@XmlRootElement( name = "deployment" )
@XmlAccessorType(XmlAccessType.FIELD)
public class DeployedWorkDayDesc
{
    private Long id;
    private WorkDay workDay;
    @XmlElementWrapper(name = "processes")
    @XmlElement(name = "process")
    private List<DeployedProcessDesc> processes = new ArrayList<DeployedProcessDesc>();
    private String deploymentId;

    public DeployedWorkDayDesc()
    {
    }

    public DeployedWorkDayDesc(DemoDeployment bd)
    {
        this.id = bd.getId();
        this.workDay = bd.getWorkDay();
        this.deploymentId = bd.getDeployIdentifier();
    }

    public void addProcess(DeployedProcessDesc dp)
    {
        dp.setDeployedWorkDay(this);
        processes.add(dp);
    }

    public String getDeploymentId()
    {
        return deploymentId;
    }

    public Long getId()
    {
        return id;
    }

    public WorkDay getWorkDay()
    {
        return workDay;
    }

    public List<DeployedProcessDesc> getProcesses()
    {
        return processes;
    }
}
