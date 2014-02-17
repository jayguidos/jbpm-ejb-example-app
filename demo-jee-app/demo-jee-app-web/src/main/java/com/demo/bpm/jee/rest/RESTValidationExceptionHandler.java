package com.demo.bpm.jee.rest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


import com.demo.bpm.jee.rest.dto.ErrorResponse;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import org.hibernate.validator.method.MethodConstraintViolationException;

@Provider
public class RESTValidationExceptionHandler
        implements ExceptionMapper<ValidationException>
{

    @Override
    public Response toResponse(ValidationException ve)
    {
        ErrorResponse errorResponse;

        if (ve instanceof ConstraintViolationException)
        {
            ConstraintViolationException cve = (ConstraintViolationException) ve;
            ConstraintViolation<?> violation = cve.getConstraintViolations().iterator().next();
            errorResponse = new ErrorResponse(500, "Validation error: " + violation.getMessage());
        }
        else if (ve instanceof MethodConstraintViolationException)
        {
            // This is a Hibernate-specific class - must not be supported yet in JSR 303
            MethodConstraintViolationException vel = (MethodConstraintViolationException) ve;
            ConstraintViolation<?> violation = ((MethodConstraintViolationException) ve).getConstraintViolations().iterator().next();
            errorResponse = new ErrorResponse(500, "Validation error: " + violation.getMessage());
        }
        else
            errorResponse = new ErrorResponse(500, "Validation error: " + ve.getMessage());

        return Response.serverError().entity(errorResponse).type(APPLICATION_XML).build();
    }
}
