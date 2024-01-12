package com.moddy.server.domain.hair_service_record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ServiceRecordTerm {
    UNDER_ONE("1 개월 미만"), ONE_THREE("1 - 3 개월"), FOUR_SIX("4 - 6 개월"), SEVEN_TWELVE("7 - 12 개월"), ABOVE_TWELVE("12 개월 초과");

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ServiceRecordTerm findByServiceRecord(String serviceRecordTerm) {
        return Stream.of(ServiceRecordTerm.values())
                .filter(c -> c.name().equals(serviceRecordTerm))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_HAIR_SERVICE_RECORD_TERM_EXCEPTION));
    }
}
