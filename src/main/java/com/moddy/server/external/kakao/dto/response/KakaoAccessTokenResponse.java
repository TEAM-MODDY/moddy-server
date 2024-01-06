package com.moddy.server.external.kakao.dto.response;

public record KakaoAccessTokenResponse(String accessToken, String refreshToken) {
    public static KakaoAccessTokenResponse of(String accessToken, String refreshToken) {
        return new KakaoAccessTokenResponse(accessToken, refreshToken);
    }
}
