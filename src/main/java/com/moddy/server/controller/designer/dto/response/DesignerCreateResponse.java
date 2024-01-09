package com.moddy.server.controller.designer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "디자이너 회원가입 뷰 Response DTO")
public record DesignerCreateResponse(
        String accessToken,
        String refreshToken
) {

}
