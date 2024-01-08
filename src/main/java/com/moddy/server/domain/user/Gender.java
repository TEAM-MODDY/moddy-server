package com.moddy.server.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Gender {
    MALE("남성"), FEMALE("여성");
    @Getter
    private final String value;
}
