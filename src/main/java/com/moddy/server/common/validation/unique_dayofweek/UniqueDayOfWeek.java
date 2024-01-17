package com.moddy.server.common.validation.unique_dayofweek;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UniqueDayOfWeekValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDayOfWeek {
    String message() default "List elements must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
