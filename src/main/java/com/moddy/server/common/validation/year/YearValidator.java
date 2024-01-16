package com.moddy.server.common.validation.year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YearValidator implements ConstraintValidator<ValidYear, String> {

    @Override
    public void initialize(ValidYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(String year, ConstraintValidatorContext context) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            Date parsedDate = dateFormat.parse(year);

            // 현재 날짜와 비교하여 과거 여부를 확인
            return parsedDate != null && parsedDate.before(new Date());
        } catch (ParseException e) {
            // 날짜 변환 실패 시 예외 처리
            return false;
        }
    }
}