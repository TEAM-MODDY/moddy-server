package com.moddy.server.common.validation.check_boolean;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BooleanValidator implements ConstraintValidator<ValidBoolean, Boolean> {

    @Override
    public void initialize(ValidBoolean constraintAnnotation) {
    }

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return value != null && (value.equals(Boolean.TRUE) || value.equals(Boolean.FALSE));
    }

}