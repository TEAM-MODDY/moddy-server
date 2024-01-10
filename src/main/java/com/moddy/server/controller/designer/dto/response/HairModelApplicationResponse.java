package com.moddy.server.controller.designer.dto.response;

import java.util.List;

public record HairModelApplicationResponse(
        int applicationId,
        String name,
        int age,
        String imgUrl,
        String gender,
        List<String> preferHairStyles
) {
}
