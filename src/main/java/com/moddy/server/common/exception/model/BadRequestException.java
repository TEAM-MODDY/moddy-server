package com.moddy.server.common.exception.model;

import com.moddy.server.common.exception.enums.ErrorCode;

public class BadRequestException extends ModdyException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
