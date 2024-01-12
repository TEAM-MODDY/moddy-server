package com.moddy.server.domain.hair_service_record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.BadRequestException;
import com.moddy.server.domain.hair_model_application.HairLength;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ServiceRecord {
    PERM("펌"), DECOLOR("탈색"), BLACK("블랙 염색"), COLOR("컬러 염색");

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ServiceRecord findByServiceRecord(String serviceRecord) {
        return Stream.of(ServiceRecord.values())
                .filter(c -> c.name().equals(serviceRecord))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_HAIR_SERVICE_RECORD_EXCEPTION));
    }
}
