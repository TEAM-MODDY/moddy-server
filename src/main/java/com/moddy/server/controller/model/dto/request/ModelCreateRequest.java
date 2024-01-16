package com.moddy.server.controller.model.dto.request;

import com.moddy.server.common.validation.year.ValidYear;
import com.moddy.server.common.validation.prefer_regions.ValidPreferRegions;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@NotNull
public record ModelCreateRequest(
        @Schema(description = "모델 회원가입 유저 이름 예시입니다.", example ="안현주")
        @NotBlank
        @Size(min = 1, max = 10, message = "name은 1~10 글자수 사이의 글자입니다.")
        @Pattern(regexp = "^[^\\s]+$", message = "name에는 공백(whitespaces)이 들어갈 수 없습니다.")
        String name,
        @Schema(description = "모델 회원가입 유저 나이 예시입니다.", example ="2000")
        @NotBlank
        @ValidYear
        String year,
        @Schema(description = "모델 회원가입 유저 성별 예시입니다.", example ="FEMALE")
        @Enumerated(EnumType.STRING)
        Gender gender,
        @Schema(description = "모델 회원가입 유저 전화번호 예시입니다.", example ="01012345678")
        @Pattern(regexp = "^010[0-9]{8}$", message = "phoneNumber는 01011112222형태입니다.")
        String phoneNumber,
        @Schema(description = "모델 회원가입 유저 마케팅 동의 여부 예시입니다.", example = "true")
        boolean isMarketingAgree,
        @Schema(description = "모델 회원가입 선호 지역 예시입니다.", example ="[\"3\", \"15\"]")
        @ValidPreferRegions
        List<Long> preferRegions
) {
}
