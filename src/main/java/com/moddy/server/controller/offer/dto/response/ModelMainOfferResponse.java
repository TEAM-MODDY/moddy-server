package com.moddy.server.controller.offer.dto.response;

import com.moddy.server.controller.model.dto.response.OfferResponse;
import com.moddy.server.domain.model.ModelApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "모델 메인 뷰 제안서 Response DTO")
public record ModelMainResponse(
        int page,
        int size,
        long total,
        ModelApplyStatus status,
        String name,
        List<OfferResponse> offers
) {

}
