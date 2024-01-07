package com.moddy.server.external.kakao.service;


import com.moddy.server.external.kakao.dto.response.KakaoAccessTokenResponse;
import com.moddy.server.external.kakao.dto.response.KakaoUserResponse;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.controller.auth.dto.request.SocialLoginRequest;
import com.moddy.server.controller.auth.dto.response.SocialLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KakaoSocialService extends SocialService {

    @Value("${kakao.client-id}")
    private String clientId;


    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;

    @Override
    public SocialLoginResponse login(SocialLoginRequest request) {


        // Authorization code로 Access Token 불러오기
        KakaoAccessTokenResponse tokenResponse = kakaoAuthApiClient.getOAuth2AccessToken(
                "authorization_code",
                clientId,
                "http://localhost:3000/login/oauth2/code/kakao",
                request.code()
        );

        // Access Token으로 유저 정보 불러오기
        KakaoUserResponse userResponse = kakaoApiClient.getUserInformation("Bearer " + tokenResponse.accessToken());

        return new SocialLoginResponse(String.valueOf(userResponse.id()));

    }
}
