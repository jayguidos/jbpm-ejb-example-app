package com.demo.bpm.jee.data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


import com.demo.bpm.facts.model.WorkDay;

@RequestScoped
public class WorkDayProducer
{
    @Inject
    private EntityManager em;

    private Map<Date, WorkDay> workDays = new HashMap<Date, WorkDay>();

    public void onWorkDayListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final WorkDay workDay)
    {
        retrieveAllWorkDays();
    }

    @PostConstruct
    public void retrieveAllWorkDays()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WorkDay> criteria = cb.createQuery(WorkDay.class);
        criteria.select(criteria.from(WorkDay.class));
        List<WorkDay> days = em.createQuery(criteria).getResultList();
        workDays.clear();
        for (WorkDay day : days)
            workDays.put(day.getDate(), day);
    }

    @Produces
    @Named
    public Map<Date, WorkDay> workDays()
    {
        return Collections.unmodifiableMap(workDays);
    }

}
