package com.moddy.server.external.kakao.service;


import com.moddy.server.request.SocialLoginRequest;
import com.moddy.server.response.SocialLoginResponse;

public abstract class SocialService {
    public abstract SocialLoginResponse login(SocialLoginRequest request);

}