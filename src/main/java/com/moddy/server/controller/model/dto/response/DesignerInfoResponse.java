package com.moddy.server.controller.model.dto.response;

import java.util.List;

public record DesignerInfoResponse(
        String imgUrl,
        String shopName,
        String name,
        String instagramUrl,
        String naverPlaceUrl,
        String introduction,
        String gender,
        List<String> dayoffs,
        String shopAddress,
        String shopDetailAddress
) {
}