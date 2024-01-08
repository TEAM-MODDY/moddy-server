package com.moddy.server.controller.model.dto.response;

import com.moddy.server.common.dto.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "모델 메인 뷰 Response DTO")
public record ModelMainResponse(
        PageInfo pageInfo,
        String status,
        String userName,
        List<OfferResponse> offer
) {
}
