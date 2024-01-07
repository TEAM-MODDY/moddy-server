package com.moddy.server.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 401
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인 해주세요."),

    // 500
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
