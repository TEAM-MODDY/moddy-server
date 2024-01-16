package com.moddy.server.domain.prefer_region.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class PreferRegionsValidator implements ConstraintValidator<ValidPreferRegions, List<Long>> {

    @Override
    public void initialize(ValidPreferRegions constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Long> preferRegions, ConstraintValidatorContext context) {

        int zeroCount = 0;
        int oneToFifteenCount = 0;

        for (Long region : preferRegions) {
            if (region == 0) {
                zeroCount++;
                if (zeroCount > 1) {
                    return false;
                }
            } else if (region >= 1 && region <= 25) {
                oneToFifteenCount++;
                if (oneToFifteenCount > 3) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }
}