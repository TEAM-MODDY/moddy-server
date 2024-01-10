package com.moddy.server.controller.designer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "디자이너 메인 뷰 Response DTO")
public record DesignerMainResponse(
        int page,
        int size,
        String name,
        List<HairModelApplicationResponse> applications
) {
}
