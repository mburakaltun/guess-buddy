package com.mburakaltun.guessbuddy.prediction.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PredictionErrorCode implements ErrorCode {
    PREDICTION_NOT_FOUND("PREDICTION_0001");

    private final String code;
}
