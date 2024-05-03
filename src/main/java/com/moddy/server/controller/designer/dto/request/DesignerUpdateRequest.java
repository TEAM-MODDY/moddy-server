package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.common.validation.unique_dayofweek.UniqueDayOfWeek;
import com.moddy.server.controller.designer.dto.HairShopDto;
import com.moddy.server.controller.designer.dto.PortfolioDto;
import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@NotNull
public record DesignerUpdateRequest(
        @Schema(example = "introduction")
        @NotNull
        @Size(min = 0, max = 200, message = "introduction은 1~200 글자수 사이의 글자입니다.")
        String introduction,
        @Schema(example = "안현주")
        @NotNull
        @Size(min = 0, max = 5, message = "name은 1~5 글자수 사이의 글자입니다.")
        String name,
        @Schema(example = "MALE")
        @NotNull
        @Enumerated(EnumType.STRING)
        Gender gender,
        @Valid
        HairShopDto hairShop,
        @UniqueDayOfWeek
        List<DayOfWeek> dayOffs,
        @Valid
        PortfolioDto portfolio,
        @Schema(example = "http://.kakao")
        @NotNull
        String kakaoOpenChatUrl
) {
}
