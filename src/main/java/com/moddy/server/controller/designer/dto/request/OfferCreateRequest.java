package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.common.validation.unique_prefer_offer_condition.UniqueOfferCondition;
import com.moddy.server.domain.prefer_offer_condition.OfferCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OfferCreateRequest(
        @Size(min = 1, max = 200, message = "상세제안은 1~200 글자수 사이의 글자입니다.")
        @NotBlank
        String offerDetail,
        @UniqueOfferCondition
        @Size(min = 1, message = "최소 한 개 이상의 희망 제안 조건이 필요합니다")
        List<OfferCondition> preferOfferConditions
) {
}
