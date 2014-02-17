package com.demo.bpm.jee.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


import com.demo.bpm.facts.model.WorkDay;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import org.hibernate.validator.constraints.NotEmpty;

// JPA Annotations
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(uniqueConstraints =
               {
                       @UniqueConstraint(columnNames = "deployIdentifier")
               }
)
// JAXB Annotations
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DemoDeployment
{
    @NotNull
    @NotEmpty
    private String deployIdentifier;

    @NotNull
    @NotEmpty
    private String artifactId;

    @NotNull
    @NotEmpty
    private String version;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "work_day_id")
    private WorkDay workDay;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @OneToMany(cascade = ALL, mappedBy = "deployment", fetch = EAGER)
    @XmlElementWrapper(name = "processes")
    @XmlElement(name = "process")
    private Set<DemoProcessInvocation> processHistory;
    @NotNull
    private boolean active;

    public DemoDeployment()
    {
    }

    public void completeProcess(DemoProcessInvocation p)
    {
        p.setEndTime(new Date());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof DemoDeployment)) return false;

        DemoDeployment that = (DemoDeployment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void startProcess(DemoProcessInvocation p)
    {
        p.setDeployment(this);
        p.setStartTime(new Date());
        this.processHistory.add(p);
    }

    @Override
    public String toString()
    {
        return "DemoDeployment{" +
                "id=" + id +
                ", deployIdentifier='" + deployIdentifier + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", workDay=" + workDay +
                '}';
    }

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

    public WorkDay getWorkDay()
    {
        return workDay;
    }

    public void setWorkDay(WorkDay workDay)
    {
        this.workDay = workDay;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Set<DemoProcessInvocation> getProcessHistory()
    {
        return processHistory;
    }

    public void setProcessHistory(Set<DemoProcessInvocation> processes)
    {
        this.processHistory = processes;
    }

    public String getDeployIdentifier()
    {
        return deployIdentifier;
    }

    public void setDeployIdentifier(String deployIdentifier)
    {
        this.deployIdentifier = deployIdentifier;
    }
}
