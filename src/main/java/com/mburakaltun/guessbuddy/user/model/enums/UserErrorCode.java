package com.mburakaltun.guessbuddy.user.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("USER_0001"),
    USERNAME_ALREADY_EXISTS("USER_0002"),
    PASSWORD_INCORRECT("USER_0003"),
    PASSWORD_MISMATCH("USER_0004"),
    USER_CANNOT_BLOCK_SELF("USER_0005"),
    USER_CANNOT_UNBLOCK_SELF("USER_0006"),
    USER_BLOCK_NOT_FOUND("USER_0007");

    private final String code;
}