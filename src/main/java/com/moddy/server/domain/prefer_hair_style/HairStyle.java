package com.moddy.server.domain.prefer_hair_style;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.BadRequestException;
import com.moddy.server.domain.hair_model_application.HairLength;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum HairStyle {
    NORMAL_CUT("일반 커트"), ALL_COLOR("전체 염색"), ALL_DECOLOR("전체 탈색"), SETTING_PERM("셋팅펌"), NORMAL_PERM("일반펌"), STRAIGHTENING("매직");

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static HairStyle findByHairStyle(String hairStyle) {
        return Stream.of(HairStyle.values())
                .filter(c -> c.name().equals(hairStyle))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_HAIR_STYLE_EXCEPTION));
    }

}

