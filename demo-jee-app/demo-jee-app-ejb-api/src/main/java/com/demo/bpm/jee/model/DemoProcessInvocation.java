package com.demo.bpm.jee.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


import static javax.persistence.GenerationType.IDENTITY;

// JPA Annotations
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(uniqueConstraints =
               {
                       @UniqueConstraint(columnNames = "kieInstanceId")
               }
)

// JAXB Annotations
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class DemoProcessInvocation
{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "WORK_DEPLOY_ID")
    @XmlTransient
    private DemoDeployment deployment;
    @NotNull
    private long kieInstanceId;
    @NotNull
    private String kieProcessDescriptionId;
    // nullable
    private Date startTime;
    // nullable
    private Date endTime;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof DemoProcessInvocation)) return false;

        DemoProcessInvocation that = (DemoProcessInvocation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    // reset the transient marked by @XmlTransient
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        this.deployment = (DemoDeployment)parent;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    @Override
    public String toString()
    {
        return "DemoProcessInvocation{" +
                "id=" + id +
                ", kieInstanceId=" + kieInstanceId +
                ", kieProcessDescriptionId='" + kieProcessDescriptionId + '\'' +
                ", deployment=" + deployment +
                '}';
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public DemoDeployment getDeployment()
    {
        return deployment;
    }

    public void setDeployment(DemoDeployment deployment)
    {
        this.deployment = deployment;
    }

    public long getKieInstanceId()
    {
        return kieInstanceId;
    }

    public void setKieInstanceId(long kieInstanceId)
    {
        this.kieInstanceId = kieInstanceId;
    }

    public String getKieProcessDescriptionId()
    {
        return kieProcessDescriptionId;
    }

    public void setKieProcessDescriptionId(String kieProcessDescriptionId)
    {
        this.kieProcessDescriptionId = kieProcessDescriptionId;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
}
