package com.moddy.server.service.auth;

import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.auth.dto.response.LoginResponseDto;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.moddy.server.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final KakaoSocialService kakaoSocialService;
    private final UserRepository userRepository;

    public LoginResponseDto login(final String baseUrl, final String kakaoCode) {
        String kakaoId = kakaoSocialService.getIdFromKakao(baseUrl, kakaoCode);
        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        TokenPair tokenPair = jwtService.generateTokenPair(String.valueOf(user.getId()));

        return new LoginResponseDto(tokenPair.accessToken(), tokenPair.refreshToken(), user.getRole().name());
    }
}
