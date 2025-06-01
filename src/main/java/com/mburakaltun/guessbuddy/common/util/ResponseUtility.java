package com.mburakaltun.guessbuddy.common.util;

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

    public <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    public <T> ApiResponse<T> error(String message, int errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .build();
    }

    public <T> ApiResponse<T> error() {
        return ApiResponse.<T>builder()
                .success(false)
                .build();
    }
}
