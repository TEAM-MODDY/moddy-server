package com.moddy.server.controller.auth;

import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.controller.auth.dto.response.LoginResponseDto;
import com.moddy.server.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.moddy.server.common.exception.enums.SuccessCode.SOCIAL_LOGIN_SUCCESS;

@Tag(name = "로그인 및 회원 가입", description = "로그인 및 회원 가입 관련 API 입니다.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public SuccessResponse<LoginResponseDto> login(@RequestHeader("kakaoCode") String kakaoCode) {
        return SuccessResponse.success(SOCIAL_LOGIN_SUCCESS, authService.login(kakaoCode));
    }
}
