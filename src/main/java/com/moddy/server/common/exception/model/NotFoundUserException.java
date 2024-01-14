package com.moddy.server.common.exception.model;

import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundUserException extends ModdyException {
    private final TokenPair tokenPair;

    public NotFoundUserException(ErrorCode errorCode, TokenPair tokenPair) {
        super(errorCode);
        this.tokenPair = tokenPair;
    }
}
