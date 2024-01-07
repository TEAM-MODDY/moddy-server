package com.moddy.server.common.exception.model;

import com.moddy.server.common.exception.enums.ErrorCode;

public class ConflictException extends ModdyException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
