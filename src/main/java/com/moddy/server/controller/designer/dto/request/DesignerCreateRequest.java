package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.controller.designer.dto.HairShopDTO;
import com.moddy.server.controller.designer.dto.PortfolioDTO;
import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record DesignerCreateRequest(
        String name,
        Gender gender,
        String phoneNumber,
        Boolean isMarketingAgree,
        HairShopDTO hairShop,
        PortfolioDTO portfolio,
        String introduction,
        String kakaoOpenChatUrl,
        List <DayOfWeek> dayOffs

) {
}
