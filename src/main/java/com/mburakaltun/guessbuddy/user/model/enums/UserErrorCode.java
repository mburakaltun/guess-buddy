package com.mburakaltun.guessbuddy.user.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("USER_0001"),
    USERNAME_ALREADY_EXISTS("USER_0002");

    private final String code;
}