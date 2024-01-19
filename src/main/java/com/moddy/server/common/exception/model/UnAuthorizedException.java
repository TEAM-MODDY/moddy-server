package com.moddy.server.common.exception.model;

import com.moddy.server.common.exception.enums.ErrorCode;

public class UnAuthorizedException extends ModdyException {
    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
