package com.demo.bpm.facts;

import com.demo.bpm.facts.model.AbstractFact;

public interface FactSessionManager
{
    public void add(AbstractFact fact);

    public void addOrUpdate(AbstractFact fact);

    public boolean contains(AbstractFact fact);

    public void delete(AbstractFact fact);

    public <T extends AbstractFact> T find(Class<T> factClass);

    public <T extends AbstractFact> T find(Class<T> factClass, String name);

    public void update(AbstractFact fact);

}
