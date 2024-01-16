package com.moddy.server.domain.designer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = YearValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYear {

    String message() default "Year는 오늘의 년도보다 큰 년도일 수 없어요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}