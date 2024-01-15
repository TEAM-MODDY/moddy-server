package com.moddy.server.common.dto;

import com.moddy.server.common.exception.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDataResponse<T> {
    private final int code;
    private final String message;
    private final T data;

    public static <T> ErrorDataResponse<T> error(ErrorCode errorCode, T data) {
        return new ErrorDataResponse<>(errorCode.getHttpStatus().value(), errorCode.getMessage(), data);
    }
}
