package com.moddy.server.controller.model.dto.request;

import com.moddy.server.common.validation.prefer_hair_style.ValidPreferHairStyles;
import com.moddy.server.domain.hair_model_application.HairLength;
import java.util.List;

import com.moddy.server.domain.prefer_hair_style.HairStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ModelApplicationRequest(
        @Schema(description = "모델의 현재 머리 기장 예시입니다.", example ="SHORT")
        @Enumerated(EnumType.STRING)
        HairLength hairLength,
        @Schema(description = "PreferHaireStyle의 예시입니다.", example ="[\"NORMAL_CUT\", \"ALL_COLOR\"]")
        @ValidPreferHairStyles
        List<HairStyle> preferHairStyles,
        @Schema(description = "모델이 원하는 헤어스타일 예시입니다.", example = "안녕하세요 저는 숱을 많이 친 허쉬컷이 하고 싶어요 근데 머리가 곱슬이라 매직도 같이 해야지 이쁘게 될것 같아요. 그리고 머리가 얇아서 그거 감안하고 해야할것 같습니다.")
        @Size(min = 0, max = 200, message = "hairDetail은 0~200 글자수 사이의 글자입니다.")
        @NotBlank
        String hairDetail,
        @Size(min = 0, max = 3, message = "hairServiceRecord는 선택사항이며, 3개까지 추가 가능합니다.")
        List<ModelHairServiceRequest> hairServiceRecords,
        @Schema(description = "모델의 인스타그램 예시입니다.", example ="hizo0")
        @Pattern(regexp = "^[^@\\s]*[_\\.]*[^\\s]+$", message = "인스타 그램 아이디에는 @는 들어올 수 없지만 _와 .는 가능합니다.")
        String instagramId
) {
    public List<ModelHairServiceRequest> getHairServiceRecords() {
        return hairServiceRecords;
    }
}

