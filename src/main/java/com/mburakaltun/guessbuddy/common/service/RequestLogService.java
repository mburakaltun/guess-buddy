package com.mburakaltun.guessbuddy.common.service;

import com.mburakaltun.guessbuddy.common.model.entity.RequestLogEntity;
import com.mburakaltun.guessbuddy.common.model.request.CreateRequestLogRequest;
import com.mburakaltun.guessbuddy.common.model.response.CreateRequestLogResponse;
import com.mburakaltun.guessbuddy.common.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class RequestLogService {
    private final RequestLogRepository requestLogRepository;

    @Async
    public CompletableFuture<CreateRequestLogResponse> createRequestLog(CreateRequestLogRequest createRequestLogRequest) {
        RequestLogEntity requestLogEntity = new RequestLogEntity();
        requestLogEntity.setRequestUrl(createRequestLogRequest.getRequestUrl());
        requestLogEntity.setRequestMethod(createRequestLogRequest.getRequestMethod());
        requestLogEntity.setRequestIp(createRequestLogRequest.getRequestIp());
        requestLogEntity.setRequestPayload(createRequestLogRequest.getRequestPayload());
        requestLogEntity.setResponsePayload(createRequestLogRequest.getResponsePayload());
        requestLogEntity.setResponseStatus(createRequestLogRequest.getResponseStatus());
        requestLogEntity.setResponseTime(createRequestLogRequest.getResponseTime());
        requestLogEntity = requestLogRepository.save(requestLogEntity);
        CreateRequestLogResponse response = CreateRequestLogResponse.builder()
                .build();
        return CompletableFuture.completedFuture(response);
    }
}