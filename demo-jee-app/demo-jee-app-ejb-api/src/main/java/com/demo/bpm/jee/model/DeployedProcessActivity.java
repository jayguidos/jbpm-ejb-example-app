package com.demo.bpm.jee.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeployedProcessActivity
{

    @XmlType( name = "processState")
    public enum State
    {
        pending(0), active(1), completed(2), aborted(3), suspended(4);

        private final int id;

        public static State fromInt(int id)
        {
            for (State state : values())
                if (state.getId() == id)
                    return state;
            throw new RuntimeException("No ID for: " + id);
        }

        public static List<Integer> asIntegers()
        {
            ArrayList<Integer> vals = new ArrayList<Integer>();
            for (State state : values())
                vals.add(state.getId());
            return vals;
        }

        private State(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }
    }

    private long id;
    private State state;
    private Date timestamp;
    @XmlElementWrapper(name = "nodes")
    @XmlElement(name = "node")
    private List<DeployedProcessNodeActivity> nodeActivities = new ArrayList<DeployedProcessNodeActivity>();
    @XmlTransient
    private DeployedProcessDesc process;

    public DeployedProcessActivity()
    {
    }

    public DeployedProcessActivity(long id, int state, Date timestamp)
    {
        this.id = id;
        this.state = State.fromInt(state);
        this.timestamp = timestamp;
    }

    public void add(DeployedProcessNodeActivity dpa)
    {
        dpa.setProcessActivity(this);
        nodeActivities.add(dpa);
        Collections.sort(nodeActivities,new Comparator<DeployedProcessNodeActivity>()
        {
            @Override
            public int compare(DeployedProcessNodeActivity o1, DeployedProcessNodeActivity o2)
            {
                return o1.getDataTimeStamp().compareTo(o2.getDataTimeStamp());
            }
        });
    }

    // reset the transient marked by @XmlTransient
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        this.process = (DeployedProcessDesc) parent;
    }

    public DeployedProcessDesc getProcess()
    {
        return process;
    }

    public void setProcess(DeployedProcessDesc process)
    {
        this.process = process;
    }
}
