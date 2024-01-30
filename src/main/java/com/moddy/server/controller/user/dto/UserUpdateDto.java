package com.moddy.server.controller.user.dto;

import com.moddy.server.domain.user.Gender;
import jakarta.validation.constraints.NotNull;

@NotNull
public record UserUpdateDto(
        String name,
        Gender gender,
        String phoneNumber,
        boolean isMarketingAgree
) {
}
