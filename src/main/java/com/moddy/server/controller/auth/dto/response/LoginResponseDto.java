package com.moddy.server.controller.auth.dto.response;

public record LoginResponseDto(String accessToken, String refreshToken, String role) {
}
