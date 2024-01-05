package com.moddy.server.common.dto;

import com.moddy.server.common.exception.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private final int code;
    private final String message;

    public static ErrorResponse error(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }
}
