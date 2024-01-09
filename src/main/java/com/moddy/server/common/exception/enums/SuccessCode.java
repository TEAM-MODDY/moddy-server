package com.moddy.server.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    FIND_MODEL_MAIN_INFO_SUCCESS(HttpStatus.OK, "모델 메인 뷰 조회 성공"),
    FIND_MODEL_DETAIL_OFFER_SUCCESS(HttpStatus.OK, "제안서 조회 성공"),
    SOCIAL_LOGIN_SUCCESS(HttpStatus.OK, "카카오 로그인 성공입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
