package com.moddy.server.external.kakao.service;


import com.moddy.server.controller.auth.dto.request.SocialLoginRequest;
import com.moddy.server.controller.auth.dto.response.SocialLoginResponse;

public abstract class SocialService {
    public abstract SocialLoginResponse login(SocialLoginRequest request);

}