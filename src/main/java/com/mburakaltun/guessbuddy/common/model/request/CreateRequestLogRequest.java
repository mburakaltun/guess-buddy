package com.mburakaltun.guessbuddy.common.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestLogRequest {
    private String requestUrl;
    private String requestMethod;
    private String requestIp;
    private String requestHeaders;
    private String requestPayload;
    private String responsePayload;
    private Integer responseStatus;
    private Long responseTime;
}
