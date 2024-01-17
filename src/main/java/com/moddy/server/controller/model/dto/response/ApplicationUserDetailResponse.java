package com.moddy.server.controller.model.dto.response;

import java.util.List;

public record ApplicationUserDetailResponse(String name, String gender, int age, List<String> preferRegions) {
}
