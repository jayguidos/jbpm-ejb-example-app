package com.demo.bpm.jee.controller;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import com.demo.bpm.jee.cdi.DemoKieManager;
import com.demo.bpm.jee.data.DemoDeploymentsProducer;
import com.demo.bpm.jee.model.DemoDeployment;
import com.demo.bpm.jee.util.DemoJBPMConfiguration;
import org.kie.api.runtime.KieSession;

public class AbstractDemoController
{
    @Inject
    protected DemoKieManager kieManager;
    @Inject
    protected DemoJBPMConfiguration config;
    @PersistenceContext(unitName = "org.jbpm.domain")
    EntityManager em;
    @Inject
    private DemoDeploymentsProducer deploymentsProducer;

    public DemoDeployment findDeployment(Long wdId)
    {
        return em.find(DemoDeployment.class, wdId);
    }

    public List<DemoDeployment> getDeployments()
    {
        return deploymentsProducer.getDeployments();
    }

    protected KieSession extractKieSession(DemoDeployment dd)
    {
        return kieManager.getRuntimeEngine(dd.getDeployIdentifier()).getKieSession();
    }
}
