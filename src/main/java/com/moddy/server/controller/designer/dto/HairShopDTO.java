package com.moddy.server.controller.designer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
public record HairShopDTO(
        @Schema(example = "juno")
        String name,
        @Schema(example = "서울시 강남구")
        String address,
        @Schema(example = "선릉로 122길")
        String detailAddress
)
{


}
