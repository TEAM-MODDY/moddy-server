package com.modee.server.common.dto;

import com.modee.server.common.exception.enums.SuccessCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {
    private final int code;
    private final String message;
    private final T data;

    public static <T> SuccessResponse<T> success(SuccessCode successCode, T data) {
        return new SuccessResponse<>(successCode.getHttpStatus().value(), successCode.getMessage(), data);
    }
}
