package com.moddy.server.common.validation.unique_prefer_offer_condition;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueOfferConditionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueOfferCondition {
    String message() default "List elements must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
