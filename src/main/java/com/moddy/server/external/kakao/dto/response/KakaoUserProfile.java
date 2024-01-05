package com.moddy.server.external.kakao.dto.response;


import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class KakaoUserProfile {

    private String nickname;
    private String profileImageUrl;
}