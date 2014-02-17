package com.demo.bpm.facts.model.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = WorkDateValidator.class)
public @interface ValidWorkDate
{
    Class<?>[] groups() default {};

    String message() default "WorkDay dates must be from 2013 on";

    Class<? extends Payload>[] payload() default {};
}
