package com.moddy.server.common.dto;

import com.moddy.server.common.exception.enums.SuccessCode;
import lombok.*;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessNonDataResponse<T> {
    private final int code;
    private final String message;

    public static SuccessNonDataResponse success(SuccessCode successCode) {
        return new SuccessNonDataResponse<>(successCode.getHttpStatus().value(), successCode.getMessage());
    }
}