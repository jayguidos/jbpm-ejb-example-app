package com.demo.bpm.jee.model;

import java.util.Date;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeployedProcessNodeActivity
{

    @XmlType(name = "nodeState")
    public enum State
    {
        started, complete
    }

    private State state;
    private long id;
    private String nodeId;
    private String nodeType;
    private String nodeName;
    private Date dataTimeStamp;
    @XmlTransient
    private DeployedProcessActivity processActivity;

    public DeployedProcessNodeActivity()
    {
    }

    public DeployedProcessNodeActivity(DeployedProcessActivity processActivity, long id, String nodeId, String nodeType, String nodeName, boolean completed, Date dataTimeStamp)
    {
        this.processActivity = processActivity;
        this.id = id;
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.nodeName = nodeName;
        this.dataTimeStamp = dataTimeStamp;
        this.state = completed ? State.complete : State.started;
    }

    // reset the transient marked by @XmlTransient
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        this.processActivity = (DeployedProcessActivity) parent;
    }

    public String getNodeType()
    {
        return nodeType;
    }

    public Date getDataTimeStamp()
    {
        return dataTimeStamp;
    }

    public State getState()
    {
        return state;
    }

    public long getId()
    {
        return id;
    }

    public String getNodeId()
    {
        return nodeId;
    }

    public String getNodeName()
    {
        return nodeName;
    }

    public DeployedProcessActivity getProcessActivity()
    {
        return processActivity;
    }

    public void setProcessActivity(DeployedProcessActivity processActivity)
    {
        this.processActivity = processActivity;
    }
}
