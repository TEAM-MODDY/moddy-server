package com.moddy.server.controller.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "모델 메인 뷰 Response DTO")
public record ModelMainResponse(
        int page,
        int size,
        String status,
        String userName,
        int offerId,
        String imgUrl,
        String name,
        String shopName,
        List conditions,
        boolean isClicked
) {

}
