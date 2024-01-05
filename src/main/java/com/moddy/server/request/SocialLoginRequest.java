package com.moddy.server.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginRequest {

    private String code;

    public static SocialLoginRequest of(String code) {
        return new SocialLoginRequest(code);
    }
}
