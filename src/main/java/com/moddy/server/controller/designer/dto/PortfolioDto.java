package com.moddy.server.controller.designer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Builder
public record PortfolioDto(
        @Schema(example = "http://instagram")
        @NotNull
        String instagramUrl,
        @Schema(example = "http://naver")
        @NotNull
        String naverPlaceUrl
) {
}
