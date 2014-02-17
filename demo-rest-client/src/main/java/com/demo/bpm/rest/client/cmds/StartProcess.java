package com.demo.bpm.rest.client.cmds;

import com.demo.bpm.jee.model.DemoProcessInvocation;
import com.demo.bpm.jee.rest.dto.StartProcessRequest;
import com.demo.bpm.rest.client.BSCommand;
import static com.demo.bpm.rest.client.BSCommand.CommandType.post;
import com.demo.bpm.rest.client.JAXBHelper;
import com.demo.bpm.rest.client.RestEasyClientFactory;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

public class StartProcess
        extends BSCommand<DemoProcessInvocation>
{
    public static final String NAME = "startProcess";
    private final JAXBHelper jaxbHelper = new JAXBHelper(StartProcessRequest.class);

    public StartProcess(RestEasyClientFactory clientFactory,String uriTemplate)
    {
        super(NAME, clientFactory.makeRequest(assembleUri(uriTemplate, NAME)), post, DemoProcessInvocation.class);
    }

    @Override
    public String getResultAsString()
            throws Exception
    {
        return getResultAsXML();
    }

    @Override
    public DemoProcessInvocation getResult()
    {
        return response.getEntity(DemoProcessInvocation.class);
    }

    protected void prepareRequest(String[] args)
            throws Exception
    {

        if (args.length != 2)
            throw new RuntimeException("expected 2 args: processId deploymentId");

        StartProcessRequest dr = new StartProcessRequest();
        dr.setKieProcessId(args[0]);
        dr.setDeploymentId(args[1]);
        request.body(APPLICATION_XML, jaxbHelper.marshallIntoXML(dr));
    }

}
