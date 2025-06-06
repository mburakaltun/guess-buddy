package com.mburakaltun.guessbuddy.common.util;

import com.mburakaltun.guessbuddy.common.model.response.ApiExceptionResponse;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseUtility {

    public <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .build();
    }

    public <T> ApiExceptionResponse error(String errorMessage) {
        return ApiExceptionResponse.builder()
                .errorMessage(errorMessage)
                .build();
    }

    public <T> ApiExceptionResponse error(String errorMessage, String errorCode) {
        return ApiExceptionResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}
