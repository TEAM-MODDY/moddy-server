package com.moddy.server.controller.designer.dto.response;

import com.moddy.server.domain.region.Region;
import com.moddy.server.domain.user.Gender;

import java.util.List;

public record ModelInfoResponse(
        Long modelId,
        String name,
        String age,
        Gender gender,
        List<Region> preferRegions,
        String instagramId

) {
}
