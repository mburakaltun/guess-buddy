package com.mburakaltun.guessbuddy.common.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateRequestLogResponse {
    private Long id;
    private String requestUrl;
    private String requestMethod;
    private String requestIp;
    private String requestPayload;
    private String responsePayload;
    private Integer responseStatus;
    private Long responseTime;
    private String message;
}
