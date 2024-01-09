package com.moddy.server.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    INVALID_DAY_OF_WEEK_EXCEPTION(HttpStatus.BAD_REQUEST, "요일을 찾을 수 없습니다"),
    INVALID_GENDER_EXCEPTION(HttpStatus.BAD_REQUEST, "성별을 찾을 수 없습니다"),
    INVALID_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰을 입력했습니다."),
    EMPTY_KAKAO_CODE_EXCEPTION(HttpStatus.BAD_REQUEST, "카카오 코드 값을 입력해 주세요."),
    INVALID_KAKAO_CODE_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 카카오 코드를 입력했습니다."),

    // 401
    TOKEN_NOT_CONTAINED_EXCEPTION(HttpStatus.UNAUTHORIZED, "Access Token이 필요합니다."),
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인 해주세요."),

    //404
    NOT_FOUNT_OFFER_EXCEPTION(HttpStatus.NOT_FOUND, "제안서를 찾을 수 없습니다"),
    NOT_FOUND_MODEL_STATUS(HttpStatus.NOT_FOUND, "모델의 지원서 작성과 제안서 도착 상태를 알 수 없습니다."),
    NOT_FOUND_MODEL_INFO(HttpStatus.NOT_FOUND, "모델 정보를 찾을 수 없습니다."),
    NOT_FOUND_PREFER_OFFER_CONDITION(HttpStatus.NOT_FOUND, "선호 제안조건을 찾을 수 없습니다."),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다."),
    DESIGNER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 디자이너는 존재하지 않습니다."),
    NOT_FOUND_OFFER_EXCEPTION(HttpStatus.NOT_FOUND, "해당 제안서를 찾을 수 없습니다."),
    NOT_FOUND_APPLICATION_EXCEPTION(HttpStatus.NOT_FOUND, "해당 지원서를 찾을 수 없습니다."),

    // 405 METHOD_NOT_ALLOWED
    METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드 입니다."),

    // 500
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
