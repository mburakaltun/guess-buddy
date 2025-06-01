package com.mburakaltun.guessbuddy.common.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),;

    private final int code;
    private final String message;
}
