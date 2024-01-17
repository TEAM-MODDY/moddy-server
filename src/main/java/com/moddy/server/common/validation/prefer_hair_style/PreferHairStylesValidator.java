package com.moddy.server.common.validation.prefer_hair_style;

import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferHairStylesValidator implements ConstraintValidator<ValidPreferHairStyles, List<PreferHairStyle>> {

    @Override
    public void initialize(ValidPreferHairStyles constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<PreferHairStyle> preferHairStyles, ConstraintValidatorContext context) {

        Set<PreferHairStyle> uniqueDays = new HashSet<>(preferHairStyles);
        if(uniqueDays.size() != preferHairStyles.size()) return false;
        if (preferHairStyles.isEmpty() || preferHairStyles == null) return false;

        return true;
    }
}