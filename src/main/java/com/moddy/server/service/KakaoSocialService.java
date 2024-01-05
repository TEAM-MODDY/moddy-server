package com.moddy.server.service;



import com.moddy.server.domain.User;
import com.moddy.server.external.kakao.dto.response.KakaoAccessTokenResponse;
import com.moddy.server.external.kakao.dto.response.KakaoUserResponse;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.repository.UserJpaRepository;
import com.moddy.server.request.SocialLoginRequest;
import com.moddy.server.response.SocialLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class KakaoSocialService extends SocialService {

    @Value("${kakao.client-id}")
    private String clientId;

    private final UserJpaRepository userJpaRepository;

    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;

    @Override
    public SocialLoginResponse login(SocialLoginRequest request) {

        System.out.println(clientId);

        // Authorization code로 Access Token 불러오기
        KakaoAccessTokenResponse tokenResponse = kakaoAuthApiClient.getOAuth2AccessToken(
                "authorization_code",
                clientId,
                "http://localhost:3000/login/oauth2/code/kakao",
                request.getCode()
        );

        // Access Token으로 유저 정보 불러오기
        KakaoUserResponse userResponse = kakaoApiClient.getUserInformation("Bearer " + tokenResponse.getAccessToken());

        Optional<User> findUser = userJpaRepository.findBySocialId(userResponse.getId());

        User user;
        if (findUser.isEmpty()) {
            User newUser = User.of(

                    userResponse.getId()
            );

            userJpaRepository.save(newUser);

            user = userJpaRepository.findBySocialId(userResponse.getId()).get();
        } else {
            user = findUser.get();
        }

        return SocialLoginResponse.of((long) user.getId());
    }
}
