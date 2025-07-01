package com.mburakaltun.guessbuddy.common.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ContentFilterErrorCode implements ErrorCode {
    OBJECTIONABLE_CONTENT_DETECTED("CONTENT_FILTER_0001");

    private final String code;
}