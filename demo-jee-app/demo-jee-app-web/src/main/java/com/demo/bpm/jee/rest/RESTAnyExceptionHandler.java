package com.demo.bpm.jee.rest;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


import com.demo.bpm.jee.rest.dto.ErrorResponse;
import static java.util.logging.Level.SEVERE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

@Provider
public class RESTAnyExceptionHandler
        implements ExceptionMapper<Exception>
{

    @Inject
    Logger log;

    @Override
    public Response toResponse(Exception exception)
    {
        // Generic exception catcher
        ErrorResponse r = new ErrorResponse();
        r.setCode(100);
        r.setMessage("Unknown error : " + exception.getMessage());
        log.log(SEVERE, "REST Exception:" + exception);
        return Response.serverError().entity(r).type(APPLICATION_XML).build();
    }
}
