package com.moddy.server.controller.model.dto;

import java.util.List;

public record ApplicationModelInfoDto(
        Long modelId,
        String name,
        int age,
        String gender,
        List<String> regionList
) {
}
