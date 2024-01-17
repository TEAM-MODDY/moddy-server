package com.moddy.server.controller.designer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Builder
public record PortfolioDto(
        @Schema(example = "http://instagram")
        @NotBlank
        @Pattern(regexp = "^[^\\s]+$", message = "url에는 공백(whitespaces)이 들어갈 수 없습니다.")
        String instagramUrl,
        @Schema(example = "http://naver")
        @NotBlank
        @Pattern(regexp = "^[^\\s]+$", message = "url에는 공백(whitespaces)이 들어갈 수 없습니다.")
        String naverPlaceUrl
) {
}
