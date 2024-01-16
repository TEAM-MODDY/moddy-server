package com.moddy.server.controller.model.dto.request;

import com.moddy.server.domain.prefer_region.validation.ValidPreferRegions;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public record ModelCreateRequest(
        @Schema(description = "모델 회원가입 유저 이름 예시입니다.", example ="안현주")
        @Size(min = 1, max = 10, message = "name은 1~10 글자수 사이의 글자입니다.")
        @Pattern(regexp = "^[^\\s]+$", message = "name에는 공백(whitespaces)이 들어갈 수 없습니다.")
        String name,
        @Schema(description = "모델 회원가입 유저 나이 예시입니다.", example ="2000")
        String year,
        @Schema(description = "모델 회원가입 유저 성별 예시입니다.", example ="FEMALE")
        @Enumerated(EnumType.STRING)
        Gender gender,
        @Schema(description = "모델 회원가입 유저 전화번호 예시입니다.", example ="01012345678")
        @Pattern(regexp = "^010[0-9]{8}$", message = "phoneNumber는 01011112222형태입니다.")
        String phoneNumber,
        @Schema(description = "모델 회원가입 유저 마케팅 동의 여부 예시입니다.", example = "true")
        @NotNull
        @Pattern(regexp = "^(true|false)$", message = "isMarketingAgree는 true, false값 이여야 합니다.")
        Boolean isMarketingAgree,
        @Schema(description = "모델 회원가입 선호 지역 예시입니다.", example ="[\"3\", \"15\"]")
        @ValidPreferRegions
        List<Long> preferRegions
) {

        @Past(message = "year 는 무조건 오늘보다 과거입니다.")
        public Date getYearAsDate() {
                try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                        return dateFormat.parse(year);
                } catch (ParseException e) {
                        // 날짜 변환 실패 시 예외 처리
                        return null; // 또는 다른 적절한 처리
                }
        }


}
