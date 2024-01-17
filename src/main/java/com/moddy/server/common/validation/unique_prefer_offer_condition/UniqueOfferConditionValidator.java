package com.moddy.server.common.validation.unique_prefer_offer_condition;

import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.prefer_offer_condition.OfferCondition;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueOfferConditionValidator implements ConstraintValidator<UniqueOfferCondition, List<OfferCondition>> {
    @Override
    public void initialize(UniqueOfferCondition constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<OfferCondition> offerConditions, ConstraintValidatorContext context) {
        Set<OfferCondition> uniqueConditions = new HashSet<>(offerConditions);
        return uniqueConditions.size() == offerConditions.size();

    }
}
