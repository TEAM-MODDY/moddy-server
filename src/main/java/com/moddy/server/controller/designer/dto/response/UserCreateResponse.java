package com.moddy.server.controller.designer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 뷰 Response DTO")
public record UserCreateResponse(
        String accessToken,
        String refreshToken
) {
}
