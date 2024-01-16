package com.moddy.server.common.validation.unique_long;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueLongElementsValidator implements ConstraintValidator<UniqueLongElements, List<Long>> {

    @Override
    public void initialize(UniqueLongElements constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Long> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            return false;
        }

        Set<Long> uniqueValues = new HashSet<>(values);
        return uniqueValues.size() == values.size();
    }
}
