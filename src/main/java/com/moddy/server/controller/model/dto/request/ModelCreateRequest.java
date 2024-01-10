package com.moddy.server.controller.model.dto.request;

import com.moddy.server.domain.user.Gender;
import java.util.List;

public record ModelCreateRequest(
        String name,
        String year,
        Gender gender,
        String phoneNumber,
        Boolean isMarketingAgree,
        List<Long> preferRegions
) {
}
