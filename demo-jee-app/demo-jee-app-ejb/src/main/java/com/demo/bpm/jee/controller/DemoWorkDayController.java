package com.demo.bpm.jee.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;


import com.demo.bpm.facts.model.AbstractFact;
import com.demo.bpm.facts.model.WorkDay;
import com.demo.bpm.facts.model.WorkDone;
import com.demo.bpm.jee.data.WorkDayProducer;
import com.demo.bpm.jee.kie.WorkDayActivityReporter;
import com.demo.bpm.jee.kie.WorkDayDeploymentUnit;
import com.demo.bpm.jee.kie.WorkDayEventLogger;
import com.demo.bpm.jee.model.DemoDeployment;
import com.demo.bpm.jee.model.DemoProcessInvocation;
import com.demo.bpm.jee.model.DeployedWorkDayDesc;
import static com.demo.bpm.shared.DemoBPMConstants.DEMO_MAVEN_GROUP;
import static com.demo.bpm.shared.DemoBPMConstants.GLBL_FACT_MANAGER;
import static com.demo.bpm.shared.DemoBPMConstants.GLBL_KSESSION;
import static com.demo.bpm.shared.DemoBPMConstants.GLBL_LOG_DIR_HOME;
import com.demo.bpm.work.handlers.support.fact.KieSessionDemoFactManager;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@Stateless
public class DemoWorkDayController
        extends AbstractDemoController
{
    @Inject
    DemoProcessController dpc;
    @Inject
    private WorkDayProducer workDayProducer;
    @Inject
    private Logger log;

    public String deleteWorkDoneItem(Long wdId, Long workId)
    {
        DemoDeployment dd = findDeployment(wdId);
        if (dd == null)
            throw new RuntimeException("Could not delete work. Deployment does not exist: " + wdId);
        KieSession kieSession = extractKieSession(dd);
        KieSessionDemoFactManager factManager = new KieSessionDemoFactManager(kieSession);
        WorkDone work = factManager.get(workId);
        if (work == null)
        {
            log.warning("Work Done not found for " + dd.getWorkDay() + ": " + workId);
            return null;
        }
        else
        {
            factManager.delete(work);
            log.info("Work Done deleted for " + dd.getWorkDay() + ": " + work.getName() + "(id=" + work.getId() + ")");
            return work.toString();
        }
    }

    public DemoDeployment deployModule(WorkDay workDay, String artifactId, String version)
    {
        // deploy the module identified by the GAV
        KModuleDeploymentUnit unit = new WorkDayDeploymentUnit(workDay, DEMO_MAVEN_GROUP, artifactId, version);
        kieManager.deployUnit(unit);

        // convert demo day to a persistent version
        if (workDayProducer.workDays().containsKey(workDay.getDate()))
            workDay = workDayProducer.workDays().get(workDay.getDate());
        else
            em.persist(workDay);

        // add all facts and globals
        KieSession kieSession = kieManager.getRuntimeEngine(unit.getIdentifier()).getKieSession();
        kieSession.insert(workDay);
        kieSession.setGlobal(GLBL_KSESSION, kieSession);
        kieSession.setGlobal(GLBL_FACT_MANAGER, new KieSessionDemoFactManager(kieSession));
        kieSession.setGlobal(GLBL_LOG_DIR_HOME, config.getGlobalLogDir().toString());

        // attach some event listeners - note these are not persistent and need to be reattached on redeploy
        attachEventListeners(workDay, kieSession);

        // remember our deployment
        DemoDeployment dd = new DemoDeployment();
        dd.setDeployIdentifier(unit.getIdentifier());
        dd.setArtifactId(artifactId);
        dd.setVersion(version);
        dd.setWorkDay(workDay);
        dd.setActive(true);
        em.persist(dd);

        log.info("Deployed WorkModule " + dd);
        return dd;
    }

    public List<String> dumpAllFacts(Long wdId)
    {
        DemoDeployment dd = findDeployment(wdId);
        if (dd == null)
            throw new RuntimeException("Could not dump facts. Deployment does not exist: " + wdId);
        ArrayList<String> facts = new ArrayList<String>();
        log.info("Dumping facts for " + dd.getWorkDay());
        for (AbstractFact abstractFact : new KieSessionDemoFactManager(extractKieSession(dd)).findAll())
            facts.add(abstractFact.toString());
        return facts;
    }

    // this is only called via bootstrapping
    public void redeployOnRestart(DemoDeployment dd)
    {
        // reassemble the unit key and redeploy to rebuild all runtimes
        KModuleDeploymentUnit unit = new WorkDayDeploymentUnit(dd.getWorkDay(), DEMO_MAVEN_GROUP, dd.getArtifactId(), dd.getVersion());
        log.info("Re-deploying WorkModule " + dd);
        kieManager.deployUnit(unit);

        // reattach transient event listeners
        KieSession kieSession = kieManager.getRuntimeEngine(unit.getIdentifier()).getKieSession();
        attachEventListeners(dd.getWorkDay(),kieSession);
    }

    public DeployedWorkDayDesc reportDeploymentActivity(Long wdId, boolean withHistory)
    {
        DemoDeployment dd = findDeployment(wdId);
        if (dd == null)
            throw new RuntimeException("Could not report on activity. Deployment does not exist: " + wdId);
        return new WorkDayActivityReporter(kieManager.getRuntimeDataService(), withHistory).reportDeploymentActivity(dd);
    }

    public boolean signal(Long wdId, String signalName)
    {
        DemoDeployment dd = findDeployment(wdId);
        if (dd == null)
            throw new RuntimeException("Could not signal. Deployment does not exist: " + wdId);
        log.info("Sending signal " + signalName + " to WorkDay: " + dd);
        extractKieSession(dd).signalEvent(signalName, null);
        return true;
    }

    public boolean undeployModule(Long wdId)
    {
        DemoDeployment dd = findDeployment(wdId);
        if (dd == null)
            throw new RuntimeException("Could not undeploy. Deployment does not exist: " + wdId);

        // reassemble the unit key and undeploy
        KModuleDeploymentUnit unit = new WorkDayDeploymentUnit(dd.getWorkDay(), DEMO_MAVEN_GROUP, dd.getArtifactId(), dd.getVersion());
        kieManager.undeployUnit(unit);

        // eliminate the deployment
        dd.setActive(false);
        log.info("Undeployed WorkModule " + dd);
        return true;
    }

    protected void attachEventListeners(WorkDay workDay, KieSession kieSession)
    {
        kieSession.addEventListener(new DefaultProcessEventListener()
        {
            @Override
            public void afterProcessCompleted(ProcessCompletedEvent event)
            {
                ProcessInstance processInstance = event.getProcessInstance();
                DemoProcessInvocation process = dpc.completeProcess(processInstance.getId());
                log.info("Completed process " + processInstance.getProcessName() + "[pId=" + processInstance.getId() + "] using module " + process.getDeployment());
            }
        });
        kieSession.addEventListener(new WorkDayEventLogger(workDay));
    }
}


