package com.demo.bpm.facts.model.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


import static com.demo.bpm.shared.DemoBPMConstants.WORK_DATE_FORMAT;

public class WorkDateStringValidator
        implements ConstraintValidator<WorkDateString, String>
{

    public void initialize(WorkDateString constraintAnnotation)
    {

    }

    public boolean isValid(String candidateWorkDateAsString, ConstraintValidatorContext context)
    {
        if (candidateWorkDateAsString == null || candidateWorkDateAsString.length() == 0)
            return false;

        try
        {
            new SimpleDateFormat(WORK_DATE_FORMAT).parse(candidateWorkDateAsString);
            return true;
        } catch (ParseException ignored)
        {
            return false;
        }
    }
}
