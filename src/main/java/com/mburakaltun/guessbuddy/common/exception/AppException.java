package com.mburakaltun.guessbuddy.common.exception;

import lombok.Getter;

@Getter
public class AppException extends Exception {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
    }
}