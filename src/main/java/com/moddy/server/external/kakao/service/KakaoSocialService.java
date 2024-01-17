package com.moddy.server.external.kakao.service;

import com.moddy.server.external.kakao.dto.response.KakaoAccessTokenResponse;
import com.moddy.server.external.kakao.dto.response.KakaoUserResponse;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoSocialService extends SocialService {

    @Value("${kakao.client-id}")
    private String clientId;
    private static final String Bearer = "Bearer ";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String KAKAO_ROUTER = "/login/oauth2/code/kakao";
    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;

    @Override
    public String getIdFromKakao(String baseUrl, String kakaoCode) {
        String redirectUrl = baseUrl + KAKAO_ROUTER;
        log.info("----------------------> " + baseUrl + " <---------------------");
        KakaoAccessTokenResponse tokenResponse = kakaoAuthApiClient.getOAuth2AccessToken(
                GRANT_TYPE,
                clientId,
                redirectUrl,
                kakaoCode
        );

        KakaoUserResponse userResponse = kakaoApiClient.getUserInformation(Bearer + tokenResponse.accessToken());
        log.info("---------> kakao id is " + userResponse.id() + "<----------------");
        log.info("---------> kakao code is " + kakaoCode + "<----------------");
        return String.valueOf(userResponse.id());
    }
}
