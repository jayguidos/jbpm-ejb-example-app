package com.demo.bpm.work.handlers.support.fact;

import java.util.Collection;


import com.demo.bpm.facts.FactSessionManager;
import com.demo.bpm.facts.model.AbstractFact;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;

public class KieSessionDemoFactManager
        implements FactSessionManager
{
    // this object contains no local state, you can make many copies
    private transient final KieSession kieSession;
    private transient final FactIDFactory factIDFactory;

    public KieSessionDemoFactManager(KieSession kieSession)
    {
        this.kieSession = kieSession;

        // the fact ID factory's state is maintained within the session itself.
        for (Object o : kieSession.getObjects())
        {
            if (o instanceof SerializableFactIDFactory)
            {
                this.factIDFactory = (FactIDFactory) o;
                return;
            }
        }

        // no state yet, stick it in
        this.factIDFactory = new SerializableFactIDFactory();
        kieSession.insert(this.factIDFactory);
    }

    public void add(AbstractFact fact)
    {
        addAndReturnHandle(fact);
    }

    public FactHandle addAndReturnHandle(AbstractFact fact)
    {
        if (fact.getId() == 0)
            fact.setId(factIDFactory.getNextFactID(fact.getClass().getName()));
        else if (contains(fact))
            throw new RuntimeException("Fact already in agenda, use update or addOrUpdate: " + fact);
        return kieSession.insert(fact);
    }

    public void addOrUpdate(AbstractFact fact)
    {
        if (contains(fact))
            update(fact);
        else
            add(fact);
    }

    public boolean contains(AbstractFact fact)
    {
        return getHandle(fact) != null;
    }

    public void delete(final AbstractFact fact)
    {
        FactHandle handle = getHandle(fact);
        if (handle == null)
            throw new RuntimeException("Fact is not in agenda: " + fact);
        kieSession.delete(handle);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFact> T find(final Class<T> factClass, final String name)
    {
        org.kie.api.runtime.ObjectFilter doneTaskFilter = new org.kie.api.runtime.ObjectFilter()
        {
            public boolean accept(Object object)
            {
                return factClass.isAssignableFrom(object.getClass()) && ((AbstractFact) object).getName().equals(name);
            }
        };
        Collection<FactHandle> workDoneHandles = kieSession.getFactHandles(doneTaskFilter);
        return workDoneHandles.size() == 0 ? null : (T) kieSession.getObject(workDoneHandles.iterator().next());
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFact> T find(final Class<T> factClass)
    {
        org.kie.api.runtime.ObjectFilter factClassFilter = new org.kie.api.runtime.ObjectFilter()
        {
            public boolean accept(Object object)
            {
                return factClass.isAssignableFrom(object.getClass());
            }
        };
        Collection<FactHandle> workDoneHandles = kieSession.getFactHandles(factClassFilter);
        return workDoneHandles.size() == 0 ? null : (T) kieSession.getObject(workDoneHandles.iterator().next());
    }

    public FactHandle find(ObjectFilter filter)
    {
        Collection<FactHandle> factHandles = kieSession.getFactHandles(filter);
        return factHandles.size() == 0 ? null : factHandles.iterator().next();
    }

    @SuppressWarnings("unchecked")
    public Collection<AbstractFact> findAll()
    {
        return (Collection<AbstractFact>) kieSession.getObjects(new org.drools.core.ObjectFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof AbstractFact;
            }
        });
    }

    public void update(AbstractFact fact)
    {
        FactHandle handle = getHandle(fact);
        if (handle == null)
            throw new RuntimeException("Fact is not in agenda: " + fact);
        update(fact, handle);
    }

    public void update(AbstractFact fact, FactHandle handle)
    {
        kieSession.update(handle, fact);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFact> T get(FactHandle handle)
    {
        return handle == null ? null : (T) kieSession.getObject(handle);
    }

    public <T extends AbstractFact> T get(final Long factId)
    {
        return get(find(new ObjectFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof AbstractFact && ((AbstractFact) object).getId() == factId;
            }
        }));
    }

    private FactHandle getHandle(AbstractFact fact)
    {
        Collection<FactHandle> factHandles = kieSession.getFactHandles(makeFilter(fact));
        if (factHandles.size() == 0)
            return null;
        else if (factHandles.size() > 1)
            throw new RuntimeException("Unexpected result - more than one fact handle for the same fact.  Fact is: " + fact);
        else
            return factHandles.iterator().next();

    }

    private ObjectFilter makeFilter(final AbstractFact fact)
    {
        return new ObjectFilter()
        {
            public boolean accept(Object object)
            {
                if (!(object instanceof AbstractFact))
                    return false;
                else
                    return fact.equals(object);
            }
        };
    }
}
