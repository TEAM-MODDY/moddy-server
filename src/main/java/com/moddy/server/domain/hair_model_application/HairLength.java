package com.moddy.server.domain.hair_model_application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HairLength {
    SHORT("숏"), ABOVE_SHOULDER("단발"), UNDER_SHOULDER("어깨 아래"), UNDER_WAIST("허리 아래");

    private final String value;
}
