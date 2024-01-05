package com.moddy.server.controller;



import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.external.kakao.SocialPlatform;
import com.moddy.server.external.kakao.SocialServiceProvider;
import com.moddy.server.request.SocialLoginRequest;
import com.moddy.server.response.SocialLoginResponse;
import com.moddy.server.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final SocialServiceProvider socialServiceProvider;

    @GetMapping("/login")
    public SuccessResponse<SocialLoginResponse> login(@RequestHeader("code") String code) {
        SocialService socialService = socialServiceProvider.getSocialService(SocialPlatform.KAKAO);
        return SuccessResponse.success(SuccessCode.SOCIAL_LOGIN_SUCCESS, socialService.login(SocialLoginRequest.of(code)));
    }
}
