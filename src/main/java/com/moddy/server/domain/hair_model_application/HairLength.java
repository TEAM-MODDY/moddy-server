package com.moddy.server.domain.hair_model_application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.BadRequestException;
import com.moddy.server.domain.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum HairLength {
    SHORT("숏"), ABOVE_SHOULDER("단발"), UNDER_SHOULDER("어깨 아래"), UNDER_WAIST("허리 아래");

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static HairLength findByHairLength(String hairLength) {
        return Stream.of(HairLength.values())
                .filter(c -> c.name().equals(hairLength))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_HAIR_LENGTH_EXCEPTION));
    }
}
