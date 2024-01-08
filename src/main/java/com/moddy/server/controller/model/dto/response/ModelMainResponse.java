package com.moddy.server.controller.model.dto.response;

import com.moddy.server.common.dto.PageInfo;
import com.moddy.server.domain.model.ModelApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "모델 메인 뷰 Response DTO")
public record ModelMainResponse(
        PageInfo pageInfo,
        ModelApplyStatus status,
        String userName,
        List<OfferResponse> offers
) {

}
