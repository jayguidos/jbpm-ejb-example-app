package com.demo.bpm.rest.client;

import javax.ws.rs.core.Response;


import com.demo.bpm.jee.rest.dto.ErrorResponse;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public abstract class BSCommand<T>
{
    public enum CommandType
    {
        get, put, post, delete
    }

    private static final Logger log = Logger.getLogger(BSCommand.class);
    protected final ClientRequest request;
    protected final JAXBHelper jaxbHelper;
    private final String name;
    private final CommandType cmdType;
    protected ClientResponse<?> response;

    public BSCommand(String name, ClientRequest request, CommandType cmdType, Class<?> tClass)
    {
        this.name = name;
        this.cmdType = cmdType;
        this.request = request;
        this.jaxbHelper = new JAXBHelper(tClass);
    }

    public T runCmd()
            throws Exception
    {
        sendRequest();
        return getResult();
    }

    @Override
    public String toString()
    {
        return "BSCommand{" +
                "name='" + name + '\'' +
                '}';
    }

    protected static String assembleUri(String base, String name)
    {
        return assembleUri(base, name, new String[] {});
    }

    protected static String assembleUri(String base, String name, String[] params )
    {
        StringBuilder uri = new StringBuilder(base).append("/").append(name);
        // RESTEasy style URI with templated parameters. URI-embedded parameters
        // are in the form of [URI]/{Pname1}/{Pname2}... and will be substituted
        // during the HTTP call
        for (String p : params)
            uri.append("/").append(p).append("/{").append(p).append("}");
        return uri.toString();
    }

    abstract public String getResultAsString()
            throws Exception;

    abstract public T getResult();

    public ClientRequest getRequest()
    {
        return request;
    }

    public String getName()
    {
        return name;
    }

    public CommandType getCmdType()
    {
        return cmdType;
    }

    protected String getResultAsXML()
    {
        T result = getResult();
        try
        {
            return jaxbHelper.marshallIntoXML(result);
        } catch (Exception e)
        {
            log.error("Could not marshall as XML: " + result, e);
            return "ERROR";
        }
    }

    private T sendRequest()
            throws Exception
    {
        switch (cmdType)
        {
            case get:
                return processServerResponse(request.get());
            case put:
                return processServerResponse(request.put());
            case post:
                return processServerResponse(request.post());
            case delete:
                return processServerResponse(request.delete());
        }
        return null;
    }

    protected abstract void prepareRequest(String[] args)
            throws Exception;

    protected T processServerResponse(ClientResponse<?> response)
            throws Exception
    {
        this.response = response;
        if (response.getResponseStatus().getFamily() == Response.Status.Family.SUCCESSFUL)
            return getResult();
        else if (response.getResponseStatus().getFamily() == Response.Status.Family.SERVER_ERROR)
            throw new ErrorResponseException(response.getEntity(ErrorResponse.class));
        else
            throw new RuntimeException("Unknown error : " + response.getResponseStatus().getStatusCode());
    }

}
