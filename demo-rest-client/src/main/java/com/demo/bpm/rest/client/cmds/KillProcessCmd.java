package com.demo.bpm.rest.client.cmds;

import com.demo.bpm.jee.model.DemoProcessInvocation;
import com.demo.bpm.rest.client.BSCommand;
import static com.demo.bpm.rest.client.BSCommand.CommandType.delete;
import com.demo.bpm.rest.client.RestEasyClientFactory;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

public class KillProcessCmd
        extends BSCommand<DemoProcessInvocation>
{

    public static final String NAME = "killProcess";

    public KillProcessCmd(RestEasyClientFactory clientFactory,String uriTemplate)
    {
        super(NAME, clientFactory.makeRequest(assembleUri(uriTemplate, NAME)), delete, DemoProcessInvocation.class);
    }

    @Override
    public String getResultAsString()
            throws Exception
    {
        return getResult().toString();
    }

    @Override
    public DemoProcessInvocation getResult()
    {
        return response.getEntity(DemoProcessInvocation.class);
    }

    protected void prepareRequest(String[] args)
            throws Exception
    {
        if (args.length != 1)
            throw new RuntimeException("expected 1 args: workProcessId");
        request.body(TEXT_PLAIN, args[0].getBytes());
    }

}

