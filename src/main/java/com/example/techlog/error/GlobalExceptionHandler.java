package com.example.techlog.error;

import com.example.techlog.common.dto.CustomProblemDetail;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleCustomException(IllegalArgumentException ex) {

        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(
                statusCode, ex.getMessage() != null ? ex.getMessage() : statusCode.getReasonPhrase());

        return handleExceptionInternal(
                ex, body, new HttpHeaders(), statusCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(
                statusCode, Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
        return handleExceptionInternal(
                ex, body, new HttpHeaders(), statusCode);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex) {

        HttpStatus statusCode = HttpStatus.UNAUTHORIZED;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(
                statusCode, ex.getMessage() != null ? ex.getMessage() : statusCode.getReasonPhrase());

        return handleExceptionInternal(
                ex, body, new HttpHeaders(), statusCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnclassifiedException(
            Exception ex) {

        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(
                statusCode, statusCode.getReasonPhrase());

        return handleExceptionInternal(
                ex, body, new HttpHeaders(), statusCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode
    ) {
        if (statusCode.is5xxServerError()) {
            log.error(ex.getMessage(), ex);
        }

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", body);

        return new ResponseEntity<>(errorBody, headers, statusCode);
    }

}
