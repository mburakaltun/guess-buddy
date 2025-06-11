package com.mburakaltun.guessbuddy.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mburakaltun.guessbuddy.common.model.request.CreateRequestLogRequest;
import com.mburakaltun.guessbuddy.common.service.RequestLogService;
import com.mburakaltun.guessbuddy.common.util.StringUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Order(1)
@RequiredArgsConstructor
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final RequestLogService requestLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            String requestHeaders = getHeaders(wrappedRequest);
            String requestBody = getRequestBody(wrappedRequest);
            String responseBody = getResponseBody(wrappedResponse);

            wrappedResponse.copyBodyToResponse();

            long duration = System.currentTimeMillis() - startTime;
            logRequestResponse(wrappedRequest, wrappedResponse, requestBody, responseBody, requestHeaders, duration);
        }
    }

    private void logRequestResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper wrappedResponse,
                                    String requestBody, String responseBody, String requestHeaders, long duration) {
        CreateRequestLogRequest createRequestLogRequest = CreateRequestLogRequest.builder()
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .requestIp(request.getRemoteAddr())
                .requestHeaders(requestHeaders)
                .requestPayload(requestBody)
                .responsePayload(responseBody)
                .responseStatus(wrappedResponse.getStatus())
                .responseTime(duration)
                .build();

        requestLogService.createRequestLog(createRequestLogRequest);
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        return formatBody(content);
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        return formatBody(content);
    }

    private String getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Collections.list(request.getHeaderNames()).forEach(headerName -> headers.put(headerName, request.getHeader(headerName)));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(headers);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    private String formatBody(byte[] content) {
        if (content == null || content.length == 0) {
            return StringUtility.EMPTY;
        }
        String body = new String(content, StandardCharsets.UTF_8);
        return body.replaceAll("\\s+", " ").trim();
    }
}