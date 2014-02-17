package com.demo.bpm.rest.client.cmds;

import com.demo.bpm.jee.model.DeployedWorkDayDesc;
import com.demo.bpm.rest.client.BSCommand;
import static com.demo.bpm.rest.client.BSCommand.CommandType.get;
import com.demo.bpm.rest.client.RestEasyClientFactory;

public class ReportDeploymentStatusCmd
        extends BSCommand<DeployedWorkDayDesc>
{

    public static final String NAME = "reportDeploymentStatus";
    public static final String DEPLOYMENT_ID = "wdId";
    public static final String USE_HISTORY = "history";

    public ReportDeploymentStatusCmd(RestEasyClientFactory clientFactory, String uriTemplate)
    {
        super(
                NAME,
                clientFactory.makeRequest(assembleUri(uriTemplate, NAME, new String[] {DEPLOYMENT_ID,USE_HISTORY} )),
                get,
                DeployedWorkDayDesc.class
        );
    }

    @Override
    public String getResultAsString()
            throws Exception
    {
        return getResultAsXML();
    }

    @Override
    public DeployedWorkDayDesc getResult()
    {
        return response.getEntity(DeployedWorkDayDesc.class);
    }

    protected void prepareRequest(String[] args)
            throws Exception
    {
        if ( args.length == 2 )
        {
            request.pathParameter(DEPLOYMENT_ID, args[0]);
            request.pathParameter(USE_HISTORY, args[1]);
        }
        else
            throw new RuntimeException("expected 2 args: deploymentId true|false where you use true to get full process execution history");
    }

}

