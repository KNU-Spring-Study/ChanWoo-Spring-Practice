package com.springstudy.studypractice.exception.handler;

import com.springstudy.studypractice.exception.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RequestErrorExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseDto> jsonParseExceptionHandler(HttpServletRequest request) {
        String message = "HTTP 바디 메시지를 읽을 수 없습니다.";

        log.error("Message = {}", message);

        return createResponseEntity(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> requestMethodErrorExceptionHandler(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("메서드 Error");
        
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        String method = e.getMethod();
        String message = "'" + method + "' 메서드를 지원하지 않습니다.";

        return createResponseEntity(status, message, request);
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
