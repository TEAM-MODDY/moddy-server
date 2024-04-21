package com.moddy.server.controller.designer.dto.response;

import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import com.moddy.server.domain.user.Gender;

import java.util.List;

public record DesignerMyPageResponse(
        String profileImgUrl,
        String introduction,
        String name,
        Gender gender,
        String phoneNumber,
        HairShop hairShop,
        List<String> dayOffs,
        Portfolio portfolio,
        String kakaoOpenChatUrl
) {
}
