package com.moddy.server.controller.designer.dto;

import lombok.*;


@Builder
public record PortfolioDTO(
        String instagramUrl,
        String naverPlaceUrl
) {
}
