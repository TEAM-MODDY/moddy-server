package com.moddy.server.controller.auth.dto.request;

public record SocialLoginRequest(
        String code
) {
    public static SocialLoginRequest of(String code) {
        return new SocialLoginRequest(code);
    }
}
