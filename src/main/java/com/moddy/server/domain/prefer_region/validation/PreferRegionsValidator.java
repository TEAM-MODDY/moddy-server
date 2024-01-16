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
        if (preferRegions == null || preferRegions.isEmpty()) {
            return true; // Optional: 널 또는 빈 리스트는 허용할 수도 있습니다.
        }

        int zeroCount = 0;
        int oneToFifteenCount = 0;

        for (Long region : preferRegions) {
            if (region == 0) {
                zeroCount++;
                if (zeroCount > 1) {
                    return false; // 0이 1개 이상이면 유효성 검증 실패
                }
            } else if (region >= 1 && region <= 25) {
                oneToFifteenCount++;
                if (oneToFifteenCount > 3) {
                    return false; // 1~25 사이의 값이 3개 이상이면 유효성 검증 실패
                }
            } else {
                return false; // 0 또는 1~25 사이의 값이 아니면 유효성 검증 실패
            }
        }

        return true; // 모든 조건을 통과하면 유효성 검증 성공
    }
}