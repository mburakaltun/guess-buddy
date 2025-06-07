package com.mburakaltun.guessbuddy.authentication.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthenticationErrorCode implements ErrorCode {

    AUTHENTICATION_FAILED("AUTH_0001"),
    INVALID_CREDENTIALS("AUTH_0002"),
    EMAIL_ALREADY_EXISTS("AUTH_0003"),
    USER_NOT_FOUND("AUTH_0004"),
    PASSWORD_MISMATCH("AUTH_0005"),
    USERNAME_ALREADY_EXISTS("AUTH_0006"),
    TOKEN_NOT_FOUND("AUTH_0007"),
    TOKEN_EXPIRED("AUTH_0008"),
    ;

    private final String code;
}
