package com.moddy.server.domain.hair_service_record;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceRecord {
    PERM("펌"), DECOLOR("탈색"), BLACK("블랙 염색"), COLOR("컬러 염색");

    private final String value;
}
