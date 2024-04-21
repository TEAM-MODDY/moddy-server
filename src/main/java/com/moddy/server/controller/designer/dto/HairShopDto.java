package com.moddy.server.controller.designer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
public record HairShopDto(
        @Schema(example = "juno")
        @NotNull
        @Size(min = 0, max = 25, message = "name은 1~25 글자수 사이의 글자입니다.")
        String name,
        @Schema(example = "서울시 강남구")
        @NotNull
        String address,
        @Schema(example = "선릉로 122길")
        @NotNull
        @Size(min = 0, max = 30, message = "상세주소는 1~30 글자수 사이의 글자입니다.")
        String detailAddress
)
{
}
