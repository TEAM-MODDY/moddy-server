package com.moddy.server.common.exception.model;

import com.moddy.server.common.exception.enums.ErrorCode;

public class NotFoundException extends ModdyException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
