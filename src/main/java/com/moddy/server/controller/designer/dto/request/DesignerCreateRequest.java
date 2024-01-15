package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.controller.designer.dto.HairShopDto;
import com.moddy.server.controller.designer.dto.PortfolioDto;
import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record DesignerCreateRequest(
        @Schema(example = "박경린")
        String name,
        @Schema(example = "MALE")
        Gender gender,
        @Schema(example = "01020000000")
        String phoneNumber,
        @Schema(example = "true")
        Boolean isMarketingAgree,
        HairShopDto hairShop,
        PortfolioDto portfolio,
        @Schema(example = "introduction")
        String introduction,
        @Schema(example = "http://.kakao")
        String kakaoOpenChatUrl,
        List<DayOfWeek> dayOffs
) {
}
