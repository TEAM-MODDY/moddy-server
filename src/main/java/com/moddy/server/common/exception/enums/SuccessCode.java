package com.moddy.server.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    DESIGNER_CREATE_SUCCESS(HttpStatus.OK, "디자이너 회원가입 성공"),
    FIND_DESIGNER_MAIN_INFO_SUCCESS(HttpStatus.OK, "디자이너 메인 뷰 조회 성공"),
    MODEL_CREATE_SUCCESS(HttpStatus.OK, "모델 회원가입 성공"),
    SOCIAL_LOGIN_SUCCESS(HttpStatus.OK, "카카오 로그인 성공입니다."),
    FIND_MODEL_MAIN_INFO_SUCCESS(HttpStatus.OK, "모델 메인 뷰 조회 성공"),
    MODEL_APPLICATION_DETAil_INFO_SUCCESS(HttpStatus.OK, "지원서 가져오기 성공"),
    FIND_MODEL_DETAIL_OFFER_SUCCESS(HttpStatus.OK, "제안서 조회 성공"),
    OPEN_CHAT_GET_SUCCESS(HttpStatus.OK, "오픈채팅방 연결 성공"),
    USER_MY_PAGE_SUCCESS(HttpStatus.OK, "마이페이지 유저 정보 조회 성공입니다."),
    OFFER_ACCEPT_SUCCESS(HttpStatus.OK, "제안서 승락 성공입니다."),
    FIND_REGION_LIST_SUCCESS(HttpStatus.OK, "희망 지역 리스트 조회 성공입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
