package com.moddy.server.controller.designer.dto.response;

import com.moddy.server.domain.prefer_hair_style.HairStyle;

import java.util.List;

public record HairModelApplicationResponse(
        Long applicationId,
        String name,
        int age,
        String imgUrl,
        String gender,
        List<HairStyle> preferHairStyles
) {
}
