package com.springstudy.studypractice.exception;

import com.springstudy.studypractice.exception.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class UserExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(UserAuthException.class)
    protected ResponseEntity<ErrorResponseDto> userAuthExceptionHandler(
            UserAuthException e, HttpServletRequest request) {
        log.error("Message = {}", e.getUserValidError().getMessage());

        return createResponseEntity(
                e.getUserValidError().getHttpStatus(), e.getUserValidError().getMessage(), request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> fieldValidationExceptionHandler(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Message = {}", e.getFieldError().getDefaultMessage());

        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getFieldError().getDefaultMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseDto> jsonParseExceptionHandler(HttpServletRequest request) {
        String message = "HTTP 바디 메시지를 읽을 수 없습니다.";

        log.error("Message = {}", message);

        return createResponseEntity(HttpStatus.BAD_REQUEST, message, request);
    }

    private ResponseEntity<ErrorResponseDto> createResponseEntity(
            HttpStatus status, String message, HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        return ResponseEntity.status(status)
                .body(ErrorResponseDto.builder()
                        .status(status.value())
                        .errorType(status.name())
                        .message(message)
                        .path(requestURI)
                        .build());
    }
}
