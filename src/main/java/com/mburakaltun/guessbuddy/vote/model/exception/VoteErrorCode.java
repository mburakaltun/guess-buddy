package com.mburakaltun.guessbuddy.vote.model.exception;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VoteErrorCode implements ErrorCode {
    VOTE_NOT_FOUND(1001, "Vote not found");

    private final int code;
    private final String message;
}
