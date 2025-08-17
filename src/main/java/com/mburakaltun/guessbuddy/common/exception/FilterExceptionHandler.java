package com.mburakaltun.guessbuddy.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mburakaltun.guessbuddy.common.model.response.ApiExceptionResponse;
import com.mburakaltun.guessbuddy.common.util.ResponseUtility;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterExceptionHandler {

    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    public void handleExpiredJwtException(HttpServletResponse response, ExpiredJwtException exception, Locale locale) throws IOException {
        String errorCode = "AUTH_0011";
        String errorMessage = getLocalizedMessage(errorCode, locale);

        log.error(exception.getMessage(), exception);
        ApiExceptionResponse apiResponse = ResponseUtility.error(errorMessage, errorCode);

        writeErrorResponse(response, apiResponse, HttpStatus.UNAUTHORIZED);
    }

    private void writeErrorResponse(HttpServletResponse response, ApiExceptionResponse apiResponse, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }

    private String getLocalizedMessage(String messageKey, Locale locale) {
        try {
            return messageSource.getMessage(messageKey, null, locale);
        } catch (Exception e) {
            log.warn("Could not resolve message for key: {}", messageKey);
            return messageKey;
        }
    }
}