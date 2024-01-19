package com.moddy.server.common.dto;


import lombok.Getter;
import lombok.ToString;

public record TokenPair(
        String accessToken, String refreshToken
) {
}
