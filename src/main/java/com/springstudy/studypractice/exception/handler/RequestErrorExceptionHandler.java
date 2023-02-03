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
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RequestErrorExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseDto> jsonParseExceptionHandler(HttpServletRequest request) {
        String message = "HTTP 바디 메시지를 읽을 수 없습니다.";

        List<String> messages = new ArrayList<>();
        messages.add(message);

        log.error("Messages = {}", messages);

        return createResponseEntity(HttpStatus.BAD_REQUEST, messages, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> requestMethodErrorExceptionHandler(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        String method = e.getMethod();
        String message = "'" + method + "' 메서드를 지원하지 않습니다.";

        List<String> messages = new ArrayList<>();
        messages.add(message);

        log.error("Messages = {}", messages);

        return createResponseEntity(status, messages, request);
    }

    private ResponseEntity<ErrorResponseDto> createResponseEntity(
            HttpStatus status, List<String> messages, HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        return ResponseEntity.status(status)
                .body(ErrorResponseDto.builder()
                        .status(status.value())
                        .errorType(status.name())
                        .messages(messages)
                        .path(requestURI)
                        .build());
    }
}
