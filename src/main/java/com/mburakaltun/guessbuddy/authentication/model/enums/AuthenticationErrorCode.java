package com.mburakaltun.guessbuddy.authentication.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthenticationErrorCode implements ErrorCode {

    AUTHENTICATION_FAILED(2001, "Authentication failed"),
    INVALID_CREDENTIALS(2002, "Invalid credentials"),
    EMAIL_ALREADY_EXISTS(2003, "Email already exists"),
    USER_NOT_FOUND(2004, "User not found"),
    PASSWORD_MISMATCH(2005, "Passwords do not match");

    private final int code;
    private final String message;
}
