package com.demo.bpm.facts.model;

import java.io.Serializable;

public abstract class AbstractFact
        implements Serializable
{
    protected long id;

    protected String name;

    protected AbstractFact()
    {
    }

    public AbstractFact(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AbstractFact)) return false;

        AbstractFact abstractFact = (AbstractFact) o;

        if (id != abstractFact.id) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    protected String printNameAndId()
    {
        return name + "(id=" + id + ",type=" + this.getClass().getSimpleName() + ")";
    }

    @Override
    public String toString()
    {
        return printNameAndId();
    }
}
