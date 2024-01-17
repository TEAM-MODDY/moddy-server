package com.moddy.server.common.validation.prefer_regions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;

public class PreferRegionsValidator implements ConstraintValidator<ValidPreferRegions, List<Long>> {

    @Override
    public void initialize(ValidPreferRegions constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Long> preferRegions, ConstraintValidatorContext context) {

        if (preferRegions == null || preferRegions.isEmpty()) return false;
        if (preferRegions.size() > 3 ) return false;
        if (preferRegions.size() != new HashSet<>(preferRegions).size()) return false;
        if (preferRegions.stream().filter(p -> p == 0).count() == 1 && preferRegions.stream().anyMatch(p -> p != 0)) return false;
        if (!preferRegions.stream().allMatch(p -> p >= 0 && p <= 25)) return false;

        return true;

    }
}