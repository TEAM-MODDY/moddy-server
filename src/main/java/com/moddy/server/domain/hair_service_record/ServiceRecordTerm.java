package com.moddy.server.domain.hair_service_record;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceRecordTerm {
    UNDER_ONE("1 개월 미만"), ONE_THREE("1 - 3 개월"), FOUR_SIX("4 - 6 개월"), SEVEN_TWELVE("7 - 12 개월"), ABOVE_TWELVE("12 개월 초과");

    private final String value;
}
