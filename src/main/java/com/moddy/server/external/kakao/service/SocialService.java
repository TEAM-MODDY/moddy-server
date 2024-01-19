package com.moddy.server.external.kakao.service;


public abstract class SocialService {
    public abstract String getIdFromKakao(String baseUrl, String kakaoCode);

}