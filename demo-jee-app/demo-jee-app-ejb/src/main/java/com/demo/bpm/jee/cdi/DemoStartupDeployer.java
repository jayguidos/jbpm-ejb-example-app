package com.demo.bpm.jee.cdi;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import com.demo.bpm.jee.controller.DemoWorkDayController;
import com.demo.bpm.jee.model.DemoDeployment;


@Singleton
@Startup
public class DemoStartupDeployer
{
    @Inject
    DemoWorkDayController wdc;
    @Inject
    private EntityManagerFactory emf;
    @Inject
    private Logger log;

    @PostConstruct
    public void redeployExistingWorkDaySessions()
    {
        // I have to get my own entity manager because postConstruct is not called with a request scope
        EntityManager em = emf.createEntityManager();

        try
        {
            List<DemoDeployment> deployments = retrieveAllDeployments(em);
            if (deployments.size() > 0)
            {

                log.info("Detected " + deployments.size() + " redeployments.");
                for (DemoDeployment bd : deployments)
                    wdc.redeployOnRestart(bd);
                log.info("Redeployment phase complete");
            }
        } finally
        {
            em.close();
        }
    }

    private List<DemoDeployment> retrieveAllDeployments(EntityManager em)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DemoDeployment> criteria = cb.createQuery(DemoDeployment.class);
        Root<DemoDeployment> dd = criteria.from(DemoDeployment.class);
        criteria.select(dd)
                .where(cb.equal(dd.get("active"), true))
                .orderBy(cb.asc(dd.get("deployIdentifier")));
        return em.createQuery(criteria).getResultList();
    }

}

