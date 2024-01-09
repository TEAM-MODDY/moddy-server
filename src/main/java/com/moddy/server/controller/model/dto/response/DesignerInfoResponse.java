package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.user.Gender;

import java.util.List;

public record DesignerInfoResponse(
        String imgUrl,
        String shopName,
        String name,
        String instagramUrl,
        String naverPlaceUrl,
        String introduction,
        Gender gender,
        List<String> dayoffs,
        String shopAddress,
        String shopDetailAddress
) {
}