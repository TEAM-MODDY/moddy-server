package com.moddy.server.domain.day_off;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DayOfWeek {
    MON("월"), TUE("화"), WED("수"), THU("목"), FRI("금"), SAT("토"), SUN("일");

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DayOfWeek findByDayOfWeek(String dayOfWeek) {
        return Stream.of(DayOfWeek.values())
                .filter(c -> c.name().equals(dayOfWeek))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_DAY_OF_WEEK_EXCEPTION));
    }
}
