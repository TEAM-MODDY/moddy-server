package com.moddy.server.common.exception.model;

import com.moddy.server.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ModdyException extends RuntimeException {
    private final ErrorCode errorCode;

    public ModdyException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
