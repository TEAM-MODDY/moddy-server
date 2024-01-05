package com.moddy.server.response;




public record SocialLoginResponse(
        Long userId
) {
    public static SocialLoginResponse of(Long userId) {
        return new SocialLoginResponse(
                userId = userId
        );
    }
}
