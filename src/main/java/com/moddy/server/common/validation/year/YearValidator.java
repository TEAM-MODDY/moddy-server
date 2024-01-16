package com.moddy.server.common.validation.year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class YearValidator implements ConstraintValidator<ValidYear, String> {

    @Override
    public void initialize(ValidYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(String year, ConstraintValidatorContext context) {
        try {
            int parsedYear = Integer.parseInt(year);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            return parsedYear >= 1900 && parsedYear <= currentYear;

        } catch (NumberFormatException  e) {
            return false;
        }
    }
}