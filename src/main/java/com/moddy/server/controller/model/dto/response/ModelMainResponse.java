package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.model.ModelApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "모델 메인 뷰 Response DTO")
public record ModelMainResponse(
        int page,
        int size,
        int total,
        ModelApplyStatus status,
        String name,
        List<OfferResponse> offers
) {

}
