package com.moddy.server.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남성"), FEMALE("여성");
    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Gender findByGender(String gender) {
        return Stream.of(Gender.values())
                .filter(c -> c.getValue().equals(gender))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_GENDER_EXCEPTION));
    }
}
