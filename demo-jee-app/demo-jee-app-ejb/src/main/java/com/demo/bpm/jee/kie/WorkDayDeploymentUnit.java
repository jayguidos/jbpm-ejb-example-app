package com.demo.bpm.jee.kie;

import com.demo.bpm.facts.model.WorkDay;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;

public class WorkDayDeploymentUnit
        extends KModuleDeploymentUnit
{

    private final WorkDay workDay;

    public WorkDayDeploymentUnit(WorkDay workDay, String groupId, String artifactId, String version)
    {
        super(groupId, artifactId, version);
        this.workDay = workDay;
    }

    public WorkDayDeploymentUnit(WorkDay workDay, String groupId, String artifactId, String version, String kbaseName, String ksessionName)
    {
        super(groupId, artifactId, version, kbaseName, ksessionName);
        this.workDay = workDay;
    }

    public WorkDayDeploymentUnit(WorkDay workDay, String groupId, String artifactId, String version, String kbaseName, String ksessionName, String strategy)
    {
        super(groupId, artifactId, version, kbaseName, ksessionName, strategy);
        this.workDay = workDay;
    }

    @Override
    public String getIdentifier()
    {
        return workDay != null ? (super.getIdentifier() + ":" + workDay) : super.getIdentifier();
    }

    @Override
    public RuntimeStrategy getStrategy()
    {
        return RuntimeStrategy.SINGLETON;
    }

    public WorkDay getWorkDay()
    {
        return workDay;
    }
}


