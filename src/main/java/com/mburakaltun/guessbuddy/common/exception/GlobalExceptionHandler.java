package com.mburakaltun.guessbuddy.common.exception;

import com.mburakaltun.guessbuddy.common.model.response.ApiExceptionResponse;
import com.mburakaltun.guessbuddy.common.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private static final String SYSTEM_ERROR_MESSAGE_KEY = "SYSTEM_0000";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleException(Exception exception, Locale locale) {
        String errorCode = SYSTEM_ERROR_MESSAGE_KEY;
        String errorMessage = getLocalizedMessage(errorCode, locale);

        log.error(errorMessage, exception);
        ApiExceptionResponse response = ResponseUtility.error(errorMessage, errorCode);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        List<String> errorMessages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        ApiExceptionResponse response = ResponseUtility.error(String.join(", ", errorMessages));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiExceptionResponse> handleLoginException(LoginException exception) {
        log.error(exception.getMessage(), exception);
        ApiExceptionResponse response = ResponseUtility.error(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        ApiExceptionResponse response = ResponseUtility.error(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiExceptionResponse> handleAppException(AppException exception, Locale locale) {
        String errorCode = exception.getErrorCode().getCode();
        String errorMessage = getLocalizedMessage(errorCode, locale);

        log.error(exception.getMessage(), exception);
        ApiExceptionResponse response = ResponseUtility.error(errorMessage, exception.getErrorCode().getCode());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiExceptionResponse> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        log.error(exception.getMessage(), exception);
        ApiExceptionResponse response = ResponseUtility.error(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    public String getLocalizedMessage(String messageKey, Locale locale) {
        return messageSource.getMessage(messageKey, null, locale);
    }
}
