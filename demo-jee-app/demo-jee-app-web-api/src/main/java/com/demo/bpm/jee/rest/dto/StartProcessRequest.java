package com.demo.bpm.jee.rest.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import static javax.xml.bind.annotation.XmlAccessType.NONE;
import org.hibernate.validator.constraints.NotEmpty;

@XmlAccessorType(NONE)
@XmlRootElement(name = "startProcess")
public class StartProcessRequest
{

    @XmlElement
    @NotNull(message = "deploymentId is required")
    @NotEmpty
    @DecimalMin("1")
    private String deploymentId;

    @XmlElement
    @NotEmpty
    @NotNull(message = "processId is required")
    private String kieProcessId;

    public String getDeploymentId()
    {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId)
    {
        this.deploymentId = deploymentId;
    }

    public String getKieProcessId()
    {
        return kieProcessId;
    }

    public void setKieProcessId(String kieProcessId)
    {
        this.kieProcessId = kieProcessId;
    }
}
