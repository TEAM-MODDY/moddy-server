package com.moddy.server.domain.prefer_hair_style;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HairStyle {
    CUT("컷"), ALL_COLOR("전체염색"), ALL_DECOLOR("전체탈색"), SETTING_PERM("세팅펌"), NORMAL_PERM("일반펌"), STRAIGHTENING("매직");

    private final String value;
}
