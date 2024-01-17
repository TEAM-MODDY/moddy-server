package com.moddy.server.controller.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record VerifyCodeRequestDto(
        @Schema(example = "01020000000")
        @Pattern(regexp = "^010[0-9]{8}$", message = "phoneNumber는 01011112222형태입니다.")
        String phoneNumber,
        @Schema(example = "123456")
        @Pattern(regexp = "[0-9]{6}$", message = "verifyCode는 123456형태입니다.")
        String verifyCode
) {
}
