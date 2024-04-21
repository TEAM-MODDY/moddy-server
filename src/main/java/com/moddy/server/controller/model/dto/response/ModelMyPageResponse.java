package com.moddy.server.controller.model.dto.response;

import java.util.List;

public record ModelMyPageResponse(
        String name,
        String year,
        String gender,
        String phoneNumber,
        List<String> preferRegions
) {
}
