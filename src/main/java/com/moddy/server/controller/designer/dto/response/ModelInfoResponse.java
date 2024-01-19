package com.moddy.server.controller.designer.dto.response;

import java.util.List;

public record ModelInfoResponse(
        Long modelId,
        String name,
        Integer age,
        String gender,
        List<String> preferRegions,
        String instagramId

) {
}
