package com.mburakaltun.guessbuddy.common.util;

import com.mburakaltun.guessbuddy.common.model.response.ApiExceptionResponse;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseUtility {
    public <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .success(true)
                .build();
    }

    public <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public <T> ApiExceptionResponse error(String message) {
        return ApiExceptionResponse.builder()
                .errorMessage(message)
                .build();
    }

    public <T> ApiExceptionResponse error(String message, int errorCode) {
        return ApiExceptionResponse.builder()
                .errorMessage(message)
                .errorCode(errorCode)
                .build();
    }
}
