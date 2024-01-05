package com.moddy.server.controller;




import com.moddy.server.external.kakao.SocialServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final SocialServiceProvider socialServiceProvider;
//
//    @GetMapping("/login")
//    public SuccessResponse<SocialLoginResponse> login(@RequestHeader("code") String code) {
//        SocialService socialService = socialServiceProvider.getSocialService(SocialPlatform.KAKAO);
//        return SuccessResponse.success(SuccessCode.SOCIAL_LOGIN_SUCCESS, socialService.login(SocialLoginRequest.of(code)));
//    }
}
