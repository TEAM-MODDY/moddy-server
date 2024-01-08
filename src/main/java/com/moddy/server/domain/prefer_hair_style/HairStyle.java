package com.moddy.server.domain.prefer_hair_style;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HairStyle {
    NORMAL_CUT("일반 커트"), ALL_COLOR("전체 염색"), ALL_DECOLOR("전체 탈색"), SETTING_PERM("셋팅펌"), NORMAL_PERM("일반펌"), STRAIGHTENING("매직");

    private final String value;
}
