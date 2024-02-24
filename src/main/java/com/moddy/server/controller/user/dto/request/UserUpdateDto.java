package com.moddy.server.controller.user.dto.request;

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
