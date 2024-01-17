package com.moddy.server.common.validation.prefer_hair_style;

import com.moddy.server.domain.prefer_hair_style.HairStyle;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferHairStylesValidator implements ConstraintValidator<ValidPreferHairStyles, List<HairStyle>> {

    @Override
    public void initialize(ValidPreferHairStyles constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<HairStyle> preferHairStyles, ConstraintValidatorContext context) {

        Set<HairStyle> uniquePreferHairStyles = new HashSet<>(preferHairStyles);
        if(uniquePreferHairStyles.size() != preferHairStyles.size()) return false;
        if (preferHairStyles.isEmpty() || preferHairStyles == null) return false;

        return true;
    }
}