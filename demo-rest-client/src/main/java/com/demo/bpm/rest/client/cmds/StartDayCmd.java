package com.demo.bpm.rest.client.cmds;

import com.demo.bpm.jee.model.DemoDeployment;
import com.demo.bpm.jee.rest.dto.DeployRequest;
import com.demo.bpm.rest.client.BSCommand;
import static com.demo.bpm.rest.client.BSCommand.CommandType.post;
import com.demo.bpm.rest.client.JAXBHelper;
import com.demo.bpm.rest.client.RestEasyClientFactory;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

public class StartDayCmd
        extends BSCommand<DemoDeployment>
{

    public static final String NAME = "startDay";
    private final JAXBHelper requestJaxbHelper = new JAXBHelper(DeployRequest.class);

    public StartDayCmd(RestEasyClientFactory clientFactory,String uriTemplate)
    {
        super(NAME, clientFactory.makeRequest(assembleUri(uriTemplate, NAME)), post, DemoDeployment.class);
    }

    @Override
    public String getResultAsString()
            throws Exception
    {
        return getResultAsXML();
    }

    @Override
    public DemoDeployment getResult()
    {
        return response.getEntity(DemoDeployment.class);
    }

    protected void prepareRequest(String[] args)
            throws Exception
    {

        if (args.length != 3)
            throw new RuntimeException("expected 3 args: artifactId version workDate");

        DeployRequest dr = new DeployRequest();
        dr.setArtifactId(args[0]);
        dr.setVersion(args[1]);
        dr.setWorkDate(args[2]);
        request.body(APPLICATION_XML, requestJaxbHelper.marshallIntoXML(dr));
    }

}

