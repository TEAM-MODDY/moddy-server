package com.moddy.server.controller.auth.dto.request;

public record TokenRequestDto(String accessToken, String refreshToken) {
}
