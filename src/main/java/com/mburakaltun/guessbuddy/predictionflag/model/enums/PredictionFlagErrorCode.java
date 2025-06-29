package com.mburakaltun.guessbuddy.predictionflag.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PredictionFlagErrorCode implements ErrorCode {
    PREDICTION_ALREADY_FLAGGED("PREDICTION_FLAG_0001");

    private final String code;
}
