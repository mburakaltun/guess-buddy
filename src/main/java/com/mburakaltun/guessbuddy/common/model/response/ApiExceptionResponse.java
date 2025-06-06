package com.mburakaltun.guessbuddy.common.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiExceptionResponse {
    private String errorMessage;
    private String errorCode;
}
