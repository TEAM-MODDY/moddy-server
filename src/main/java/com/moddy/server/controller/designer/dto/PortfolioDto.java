package com.moddy.server.controller.designer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
public record PortfolioDto(
        @Schema(example = "http://instagram")
        String instagramUrl,
        @Schema(example = "http://naver")
        String naverPlaceUrl
) {
}
