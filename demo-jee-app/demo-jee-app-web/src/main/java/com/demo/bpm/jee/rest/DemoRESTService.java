package com.demo.bpm.jee.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


import com.demo.bpm.facts.model.WorkDay;
import com.demo.bpm.jee.controller.DemoProcessController;
import com.demo.bpm.jee.controller.DemoWorkDayController;
import com.demo.bpm.jee.model.DemoDeployment;
import com.demo.bpm.jee.model.DemoProcessInvocation;
import com.demo.bpm.jee.model.DeployedWorkDayDesc;
import com.demo.bpm.jee.rest.dto.WorkFactsResponse;
import com.demo.bpm.jee.rest.dto.DeployRequest;
import com.demo.bpm.jee.rest.dto.StartProcessRequest;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.resteasy.spi.validation.ValidateRequest;

@Path("/mgmt")
@RequestScoped
@ValidateRequest
public class DemoRESTService
{

    @Inject
    DemoWorkDayController wdc;
    @Inject
    private DemoProcessController pc;

    @DELETE
    @Path("/deleteWorkDone/wdId/{wdId}/workId/{workId}")
    @Produces(TEXT_PLAIN)

    public String deleteWorkDoneItem(

            @NotNull @DecimalMin("1") @PathParam("wdId") Long wdId,
            @NotNull @DecimalMin("1") @PathParam("workId") Long workId

    )
    {
        return wdc.deleteWorkDoneItem(wdId, workId);
    }

    @GET
    @Path("/deployments")
    @Produces(APPLICATION_XML)
    @Wrapped(element = "list", prefix = "deployments")

    public List<DemoDeployment> deployments()
    {
        return pc.getDeployments();
    }

    @GET
    @Path("/dumpFacts/wdId/{wdId}")
    @Produces(APPLICATION_XML)

    public WorkFactsResponse dumpFacts
            (
                    @NotNull @NotEmpty @DecimalMin("1") @PathParam("wdId") String wdIdString
            )
    {
        DemoDeployment demoDeployment = findWorkDayDeployment(wdIdString);
        WorkFactsResponse response = new WorkFactsResponse();
        response.setDemoDeploymentId(demoDeployment.getId());
        response.setFacts(wdc.dumpAllFacts(demoDeployment.getId()));
        return response;
    }

    @DELETE
    @Path("/killProcess")
    @Produces(APPLICATION_XML)
    @Consumes(TEXT_PLAIN)

    public DemoProcessInvocation killProcess
            (
                    @NotNull @DecimalMin("1") Long workProcessId
            )
    {
        return pc.killProcess(workProcessId);
    }

    @GET
    @Path("/reportDeploymentStatus/wdId/{wdId}/history/{history}")
    @Produces(APPLICATION_XML)

    public DeployedWorkDayDesc reportDeploymentStatus
            (
                    @NotNull @NotEmpty @DecimalMin("1") @PathParam("wdId") String wdIdString,
                    @PathParam("history") boolean withHistory
            )
    {
        return wdc.reportDeploymentActivity(findWorkDayDeployment(wdIdString).getId(), withHistory);
    }

    @GET
    @Path("/reportProcessStatus/wdId/{wdId}/processId/{processId}/history/{history}")
    @Produces(APPLICATION_XML)

    public DeployedWorkDayDesc reportProcessStatus
            (
                    @NotNull @NotEmpty @DecimalMin("1") @PathParam("wdId") String wdIdString,
                    @NotNull @NotEmpty @PathParam("processId") String processId,
                    @PathParam("history") boolean withHistory
            )
    {
        return pc.reportProcessActivity(findWorkDayDeployment(wdIdString).getId(), processId, withHistory);
    }

    @PUT
    @Path("/signalWorkdDay/wdId/{wdId}/signalName/{signalName}")

    public boolean signal
            (
                    @NotNull @DecimalMin("1") @PathParam("wdId") Long wdId,
                    @NotNull @NotEmpty @PathParam("signalName") String signalName
            )
    {
        return wdc.signal(wdId, signalName);
    }

    @POST
    @Path("/startDay")
    @Produces(APPLICATION_XML)
    @Consumes(APPLICATION_XML)

    public DemoDeployment startDay
            (
                    @Valid DeployRequest deploy
            )
    {
        return wdc.deployModule(new WorkDay(deploy.getWorkDate()), deploy.getArtifactId(), deploy.getVersion());
    }

    @POST
    @Path("/startProcess")
    @Produces(APPLICATION_XML)
    @Consumes(APPLICATION_XML)

    public DemoProcessInvocation startProcess
            (
                    @Valid StartProcessRequest sp
            )
    {
        // step 1 - create an invocation record in the DB - this is a separate transaction.
        DemoProcessInvocation processInvocation = pc.createProcessInvocation(findWorkDayDeployment(sp.getDeploymentId()).getId(), sp.getKieProcessId());

        // now we can start the process, which may or may not run to completion right here.
        pc.startProcess(processInvocation);

        return processInvocation;
    }

    @DELETE
    @Path("/stopDay")
    @Produces(TEXT_PLAIN)
    @Consumes(TEXT_PLAIN)

    public boolean stopDay
            (
                    @NotNull @NotEmpty @DecimalMin("1") String wdIdString
            )
    {
        return wdc.undeployModule(findWorkDayDeployment(wdIdString).getId());
    }

    private DemoDeployment findWorkDayDeployment(String wdIdStr)
    {
        Long wdId = Long.parseLong(wdIdStr);
        DemoDeployment bd = pc.findDeployment(wdId);
        if (bd == null)
            throw new RuntimeException("DemoDeployment not found with Id: " + wdId);
        else
            return bd;
    }

}
