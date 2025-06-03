package com.mburakaltun.guessbuddy.prediction.model.exception;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PredictionErrorCode implements ErrorCode {
    PREDICTION_NOT_FOUND(2001, "Prediction not found");

    private final int code;
    private final String message;
}
