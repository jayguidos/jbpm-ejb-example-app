package com.demo.bpm.jee.data;

import java.util.List;

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
import javax.persistence.criteria.Root;


import com.demo.bpm.jee.model.DemoDeployment;

@RequestScoped
public class DemoDeploymentsProducer
{
    @Inject
    private EntityManager em;

    private List<DemoDeployment> deployments;

    @Produces
    @Named
    public List<DemoDeployment> getDeployments()
    {
        return deployments;
    }

    public void onWorkDayDeploymentListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final DemoDeployment deployment)
    {
        retrieveAllDeploymentsOrderedByName();
    }

    @PostConstruct
    public void retrieveAllDeploymentsOrderedByName()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DemoDeployment> criteria = cb.createQuery(DemoDeployment.class);
        Root<DemoDeployment> deploymentRoot = criteria.from(DemoDeployment.class);
        criteria.select(deploymentRoot).orderBy(cb.asc(deploymentRoot.get("deployIdentifier")));
        deployments = em.createQuery(criteria).getResultList();
    }

}
