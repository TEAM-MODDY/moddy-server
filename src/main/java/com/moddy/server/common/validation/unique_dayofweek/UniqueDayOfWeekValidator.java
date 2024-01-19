package com.moddy.server.common.validation.unique_dayofweek;

import com.moddy.server.domain.day_off.DayOfWeek;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueDayOfWeekValidator implements ConstraintValidator<UniqueDayOfWeek, List<DayOfWeek>> {

    @Override
    public void initialize(UniqueDayOfWeek constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<DayOfWeek> dayOfWeeks, ConstraintValidatorContext context) {
        Set<DayOfWeek> uniqueDays = new HashSet<>(dayOfWeeks);
        return uniqueDays.size() == dayOfWeeks.size();

    }
}
