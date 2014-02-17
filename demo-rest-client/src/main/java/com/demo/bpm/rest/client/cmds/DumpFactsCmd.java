package com.demo.bpm.rest.client.cmds;

import com.demo.bpm.jee.rest.dto.WorkFactsResponse;
import com.demo.bpm.rest.client.BSCommand;
import static com.demo.bpm.rest.client.BSCommand.CommandType.get;
import com.demo.bpm.rest.client.RestEasyClientFactory;

public class DumpFactsCmd
        extends BSCommand<WorkFactsResponse>
{

    public static final String NAME = "dumpFacts";
    public static final String DEPLOYMENT_ID = "wdId";

    public DumpFactsCmd(RestEasyClientFactory clientFactory,String uriTemplate)
    {
        super(NAME, clientFactory.makeRequest(assembleUri(uriTemplate, NAME, new String[] {DEPLOYMENT_ID} )), get, WorkFactsResponse.class);
    }

    @Override
    public String getResultAsString()
            throws Exception
    {
        return getResultAsXML();
    }

    @Override
    public WorkFactsResponse getResult()
    {
        return response.getEntity(WorkFactsResponse.class);
    }

    protected void prepareRequest(String[] args)
            throws Exception
    {
        if (args.length != 1)
            throw new RuntimeException("expected 1 args: deploymentId");
        request.pathParameter(DEPLOYMENT_ID, args[0]);
    }

}

