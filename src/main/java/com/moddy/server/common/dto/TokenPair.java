package com.moddy.server.common.dto;

public record TokenPair(
        String accessToken, String refreshToken
) {
}
