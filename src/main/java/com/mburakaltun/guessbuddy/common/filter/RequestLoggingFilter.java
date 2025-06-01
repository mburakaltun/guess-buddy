package com.mburakaltun.guessbuddy.common.filter;

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

@Order(1)
@RequiredArgsConstructor
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final RequestLogService requestLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            String requestBody = getRequestBody(wrappedRequest);
            String responseBody = getResponseBody(wrappedResponse);

            logRequestResponse(wrappedRequest, wrappedResponse, requestBody, responseBody, duration);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper wrappedResponse, String requestBody, String responseBody, long duration) {
        CreateRequestLogRequest createRequestLogRequest = CreateRequestLogRequest.builder()
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .requestIp(request.getRemoteAddr())
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

    private String formatBody(byte[] content) {
        if (content == null || content.length == 0) {
            return StringUtility.EMPTY;
        }
        String body = new String(content, StandardCharsets.UTF_8);
        return body.replaceAll("\\s+", " ").trim();
    }
}