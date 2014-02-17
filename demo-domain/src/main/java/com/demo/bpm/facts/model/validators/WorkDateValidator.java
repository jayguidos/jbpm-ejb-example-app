package com.demo.bpm.facts.model.validators;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WorkDateValidator
        implements ConstraintValidator<ValidWorkDate, Date>
{

    public void initialize(ValidWorkDate constraintAnnotation)
    {

    }

    public boolean isValid(Date date, ConstraintValidatorContext context)
    {
        if (date == null || date.getTime() == 0)
            return false;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) < 2013 || cal.get(Calendar.YEAR) > 2030)
            return false;
        return true;
    }
}
