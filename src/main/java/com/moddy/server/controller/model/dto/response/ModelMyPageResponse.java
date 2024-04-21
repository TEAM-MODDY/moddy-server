package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.user.Gender;
import java.util.List;

public record ModelMyPageResponse(
        String name,
        String year,
        Gender gender,
        String phoneNumber,
        List<String> preferRegions
) {
}
