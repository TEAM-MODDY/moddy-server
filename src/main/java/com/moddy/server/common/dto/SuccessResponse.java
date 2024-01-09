package com.moddy.server.common.dto;

import com.moddy.server.common.exception.enums.SuccessCode;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {
    private final int code;
    private final String message;
    private T data;

    public static SuccessResponse success(SuccessCode successCode) {
        return new SuccessResponse<>(successCode.getHttpStatus().value(), successCode.getMessage());
    }

    public static <T> SuccessResponse<T> success(SuccessCode successCode, T data) {
        return new SuccessResponse<>(successCode.getHttpStatus().value(), successCode.getMessage(), data);
    }
}
