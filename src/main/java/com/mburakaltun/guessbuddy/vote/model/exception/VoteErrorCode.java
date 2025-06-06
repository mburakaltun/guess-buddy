package com.mburakaltun.guessbuddy.vote.model.exception;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VoteErrorCode implements ErrorCode {
    VOTE_NOT_FOUND("VOTE_0001");

    private final String code;
}
