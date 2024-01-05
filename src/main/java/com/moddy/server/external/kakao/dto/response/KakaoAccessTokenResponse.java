package com.moddy.server.external.kakao.dto.response;


import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class KakaoAccessTokenResponse {

    private String accessToken;
    private String refreshToken;

    public static KakaoAccessTokenResponse of(String accessToken, String refreshToken) {
        return new KakaoAccessTokenResponse(accessToken, refreshToken);
    }
}
