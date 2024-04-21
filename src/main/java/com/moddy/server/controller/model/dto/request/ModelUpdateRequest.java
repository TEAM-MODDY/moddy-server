package com.moddy.server.controller.model.dto.request;

import com.moddy.server.common.validation.prefer_regions.ValidPreferRegions;
import com.moddy.server.common.validation.year.ValidYear;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@NotNull
public record ModelUpdateRequest(
        @Schema(description = "모델 회원가입 유저 이름 예시입니다.", example ="안현주")
        @Size(min = 0, max = 5, message = "name은 1~5 글자수 사이의 글자입니다.")
        String name,
        @Schema(description = "모델 회원가입 유저 나이 예시입니다.", example ="2000")
        @NotBlank
        @ValidYear
        String year,
        @Schema(description = "모델 회원가입 유저 성별 예시입니다.", example ="FEMALE")
        @Enumerated(EnumType.STRING)
        @NotNull
        Gender gender,
        @Schema(description = "모델 회원가입 선호 지역 예시입니다.", example ="[\"3\", \"15\"]")
        @ValidPreferRegions
        List<Long> preferRegions
) {
}
