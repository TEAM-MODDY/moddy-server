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
    POST_OFFER_SUCCESS(HttpStatus.OK, "제안서 작성 성공입니다."),
    SEND_VERIFICATION_CODE_SUCCESS(HttpStatus.OK, "전화번호 인증 요청 성공입니다."),
    FIND_REGION_LIST_SUCCESS(HttpStatus.OK, "희망 지역 리스트 조회 성공입니다."),
    VERIFICATION_CODE_MATCH_SUCCESS(HttpStatus.OK, "전화번호 인증 성공입니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공입니다."),
    REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공입니다."),
    CREATE_MODEL_APPLICATION_SUCCESS(HttpStatus.OK, "모델 지원서 생성 성공입니다."),
    USER_WITHDRAW_SUCCESS(HttpStatus.OK, "회원 탈퇴 성공입니다."),
    GET_PRE_SIGNED_URL_SUCCESS(HttpStatus.OK, "제안서 다운로드 url 생성 성공"),
    GET_APPLICATION_IMG_URL_SUCCESS(HttpStatus.OK, "지원서 이미지 url 가져오기 성공"),
    CHECK_VALID_APPLICATION_SUCCESS(HttpStatus.OK, "유효한 지원서 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
