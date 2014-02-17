package com.demo.bpm.facts.model;

import java.util.Date;

public class WorkDone
        extends AbstractFact
{
    private static final long serialVersionUID = -9101928234778914389L;
    private Date doneTime = new Date();

    public WorkDone(String name)
    {
        super(name);
    }

    @Override
    public String toString()
    {
        return printNameAndId() + "{" +
                "name='" + getName() + '\'' +
                "doneTime='" + doneTime + '\'' +
                '}';
    }

    public Date getDoneTime()
    {
        return doneTime;
    }
}
