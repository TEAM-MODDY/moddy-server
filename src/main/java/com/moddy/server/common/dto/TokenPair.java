package com.moddy.server.common.dto;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public record TokenPair(
        String accessToken, String refreshToken
) {
}
