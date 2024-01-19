package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.common.validation.unique_dayofweek.UniqueDayOfWeek;
import com.moddy.server.controller.designer.dto.HairShopDto;
import com.moddy.server.controller.designer.dto.PortfolioDto;
import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema
public record DesignerCreateRequest(
        @Schema(example = "박경린")
        @NotNull
        String name,
        @Schema(example = "MALE")
        @NotNull
        @Enumerated(EnumType.STRING)
        Gender gender,
        @Schema(example = "01020000000")
        @NotBlank
        @Pattern(regexp = "^010[0-9]{8}$", message = "phoneNumber는 01011112222형태입니다.")
        String phoneNumber,
        @Schema(example = "true")
        boolean isMarketingAgree,
        @Valid
        HairShopDto hairShop,
        @Valid
        PortfolioDto portfolio,
        @Schema(example = "introduction")
        @NotNull
        @Size(min = 0, max = 200, message = "introduction은 1~200 글자수 사이의 글자입니다.")
        String introduction,
        @Schema(example = "http://.kakao")
        @NotNull
        String kakaoOpenChatUrl,
        @UniqueDayOfWeek
        List<DayOfWeek> dayOffs
) {
}
