package com.demo.bpm.rest.client.cmds;

import java.util.List;


import com.demo.bpm.jee.model.DemoDeployment;
import com.demo.bpm.rest.client.BSCommand;
import static com.demo.bpm.rest.client.BSCommand.CommandType.get;
import com.demo.bpm.rest.client.RestEasyClientFactory;
import org.jboss.resteasy.util.GenericType;

public class GetDeploymentsCmd
        extends BSCommand<List<DemoDeployment>>
{

    public static final String NAME = "deployments";
    public static final GenericType<List<DemoDeployment>> BD_LIST_TYPE = new GenericType<List<DemoDeployment>>()
    {
    };

    public GetDeploymentsCmd(RestEasyClientFactory clientFactory, String uriTemplate)
    {
        super(NAME, clientFactory.makeRequest(assembleUri(uriTemplate, NAME)), get, DemoDeployment.class);
    }

    @Override
    public String getResultAsString()
            throws Exception
    {
        return jaxbHelper.marshallIntoXML(NAME, getResult());
    }

    @Override
    public List<DemoDeployment> getResult()
    {
        return response.getEntity(BD_LIST_TYPE);
    }

    protected void prepareRequest(String[] args)
            throws Exception
    {
    }

}

