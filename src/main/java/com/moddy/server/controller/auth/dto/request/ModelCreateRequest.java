package com.moddy.server.controller.auth.dto.request;

import java.util.List;

public record ModelCreateRequest(
        String name,
        String year,
        String gender,
        String phoneNumber,
        Boolean isMarketingAgree,
        List<Long> preferRegions
) {
}
