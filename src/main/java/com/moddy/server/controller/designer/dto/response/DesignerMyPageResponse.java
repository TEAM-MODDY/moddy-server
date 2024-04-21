package com.moddy.server.controller.designer.dto.response;

import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;

import java.util.List;

public record DesignerMyPageResponse(
        String profileImgUrl,
        String introduction,
        String name,
        String gender,
        String phoneNumber,
        HairShop hairShop,
        List<String> dayOffs,
        Portfolio portfolio,
        String kakaoOpenChatUrl
) {
}
