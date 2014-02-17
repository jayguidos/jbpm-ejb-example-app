package com.demo.bpm.rest.client;

import com.demo.bpm.jee.rest.dto.ErrorResponse;

public class ErrorResponseException
        extends Exception
{
    private final ErrorResponse errorInfo;

    public ErrorResponseException(ErrorResponse errorInfo)
    {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
    }

    public ErrorResponse getErrorInfo()
    {
        return errorInfo;
    }
}
