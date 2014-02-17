package com.demo.bpm.jee.rest.dto;


import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import com.demo.bpm.facts.model.validators.WorkDateString;
import static com.demo.bpm.shared.DemoBPMConstants.WORK_DATE_FORMAT;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

@XmlAccessorType(NONE)
@XmlRootElement(name = "deploy")
public class DeployRequest
        implements Serializable
{
    @XmlElement
    @NotNull(message = "artifactId is required")
    private String artifactId;

    @XmlElement
    @NotNull(message = "version (i.e. artifact version) is required")
    private String version;

    @XmlElement
    @NotNull(message = "workDay is required in format " + WORK_DATE_FORMAT)
    @WorkDateString
    private String workDate;

    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId(String artifactId)
    {
        this.artifactId = artifactId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getWorkDate()
    {
        return workDate;
    }

    public void setWorkDate(String workDate)
    {
        this.workDate = workDate;
    }
}
