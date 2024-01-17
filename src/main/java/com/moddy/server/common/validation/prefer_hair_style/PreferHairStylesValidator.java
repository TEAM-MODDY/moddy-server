package com.moddy.server.common.validation.prefer_hair_style;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class PreferHairStylesValidator implements ConstraintValidator<ValidPreferHairStyles, List<String>> {

    @Override
    public void initialize(ValidPreferHairStyles constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> preferHairStyles, ConstraintValidatorContext context) {

        if (preferHairStyles.isEmpty() || preferHairStyles == null) return false;
        if (preferHairStyles.size() > 6) return false;

        return true;
    }
}