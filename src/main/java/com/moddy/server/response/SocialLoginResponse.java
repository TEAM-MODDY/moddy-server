package com.moddy.server.response;


public record SocialLoginResponse(
        String userId
) {
    public static SocialLoginResponse of(String userId) {
        return new SocialLoginResponse(
                userId = userId
        );
    }
}
