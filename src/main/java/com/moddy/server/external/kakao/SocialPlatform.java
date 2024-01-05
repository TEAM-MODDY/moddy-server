package com.moddy.server.external.kakao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocialPlatform {
    KAKAO("카카오"),
    GOOGLE("구글"),
    ;

    private final String value;
}
