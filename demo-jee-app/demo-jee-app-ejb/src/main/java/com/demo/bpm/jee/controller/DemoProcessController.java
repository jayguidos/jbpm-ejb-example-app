package com.demo.bpm.jee.controller;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;


import com.demo.bpm.jee.kie.WorkDayActivityReporter;
import com.demo.bpm.jee.model.DemoDeployment;
import com.demo.bpm.jee.model.DemoProcessInvocation;
import com.demo.bpm.jee.model.DeployedWorkDayDesc;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@Stateless
public class DemoProcessController
        extends AbstractDemoController
{
    @Inject
    private Logger log;

    public DemoProcessInvocation completeProcess(long pid)
    {
        // query to convert to ID of persistent object
        DemoProcessInvocation process = processInvocationFromProcessInstance(pid);
        if (process == null)
            throw new RuntimeException("Could not find Process Invocation. Kie process instance is not recorded: " + pid);
        process.getDeployment().completeProcess(process);
        return process;
    }

    // this transaction must complete BEFORE the process is started, because processes
    // can run to completion in the KIE startProcess() call and the end process listeners
    // require the process record to be in the DB
    @TransactionAttribute(REQUIRES_NEW)
    public DemoProcessInvocation createProcessInvocation(Long wdId, String pid)
    {
        DemoDeployment dd = findDeployment(wdId);
        if (dd == null)
            throw new RuntimeException("Could not start process. Deployment does not exist: " + wdId);
        KieSession kieSession = extractKieSession(dd);
        ProcessInstance processInstance = kieSession.createProcessInstance(pid, null);

        log.info("Created process " + processInstance.getProcessName() + "[pId=" + processInstance.getId() + "] using module " + dd);

        DemoProcessInvocation pi = new DemoProcessInvocation();
        pi.setDeployment(dd);
        pi.setKieProcessDescriptionId(pid);
        pi.setKieInstanceId(processInstance.getId());
        dd.startProcess(pi);

        return pi;
    }

    public DemoProcessInvocation killProcess(long pid)
    {
        DemoProcessInvocation process = em.find(DemoProcessInvocation.class, pid);
        if (process == null)
            throw new RuntimeException("Could not find Process. Cannot abort: " + pid);
        KieSession kieSession = extractKieSession(process.getDeployment());
        ProcessInstance processInstance = kieSession.getProcessInstance(process.getKieInstanceId());
        if (processInstance != null)
            try
            {
                kieSession.abortProcessInstance(process.getKieInstanceId());
                log.warning("Killed process " + processInstance.getProcessName() + "[pId=" + processInstance.getId() + "] using module " + process.getDeployment());
            } catch (Exception ignored)
            {
                log.warning("Could not kill process " + processInstance.getProcessName() + "[pId=" + processInstance.getId() + "] using module " + process.getDeployment());
            }
        else
            log.warning("Process gone, no kill done for " + processInstance.getProcessName() + "[pId=" + processInstance.getId() + "] using module " + process.getDeployment());

        process.getDeployment().completeProcess(process);
        return process;
    }

    public DeployedWorkDayDesc reportProcessActivity(Long wdId, String pid, boolean withHistory)
    {
        DemoDeployment bd = findDeployment(wdId);
        if (bd == null)
            throw new RuntimeException("Could not report on process activity. Deployment does not exist: " + wdId);
        return new WorkDayActivityReporter(kieManager.getRuntimeDataService(), withHistory).reportProcessActivity(bd, pid);
    }

    public void startProcess(DemoProcessInvocation pi)
    {
        DemoDeployment dd = pi.getDeployment();
        KieSession kieSession = extractKieSession(dd);
        ProcessInstance processInstance = kieSession.startProcessInstance(pi.getKieInstanceId());
        log.info("Started process " + processInstance.getProcessName() + "[pId=" + processInstance.getId() + "] using module " + dd);
    }

    private DemoProcessInvocation processInvocationFromProcessInstance(long pid)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DemoProcessInvocation> query = cb.createQuery(DemoProcessInvocation.class);
        Root<DemoProcessInvocation> bpi = query.from(DemoProcessInvocation.class);

        ParameterExpression<Long> instanceParameter = cb.parameter(Long.class);
        query.select(bpi).where(
                cb.equal(
                        bpi.get("kieInstanceId"), instanceParameter
                )
        );

        return em.createQuery(query).setParameter(instanceParameter, pid).getSingleResult();
    }

}
