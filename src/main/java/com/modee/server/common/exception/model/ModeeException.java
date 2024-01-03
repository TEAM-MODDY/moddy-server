package com.modee.server.common.exception.model;

import com.modee.server.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ModeeException extends RuntimeException {
    private final ErrorCode errorCode;

    public ModeeException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
