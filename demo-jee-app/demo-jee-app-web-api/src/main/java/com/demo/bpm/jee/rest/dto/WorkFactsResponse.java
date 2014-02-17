package com.demo.bpm.jee.rest.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


import static javax.xml.bind.annotation.XmlAccessType.NONE;

@XmlAccessorType(NONE)
@XmlRootElement(name = "deploymentFacts")
public class WorkFactsResponse
    implements Serializable
{

    @XmlElement
    private Long demoDeploymentId;

    @XmlElement( name="fact")
    @XmlElementWrapper( name="factList")
    private List<String> facts;

    public Long getDemoDeploymentId()
    {
        return demoDeploymentId;
    }

    public void setDemoDeploymentId(Long demoDeploymentId)
    {
        this.demoDeploymentId = demoDeploymentId;
    }

    public List<String> getFacts()
    {
        return facts;
    }

    public void setFacts(List<String> facts)
    {
        this.facts = facts;
    }
}
