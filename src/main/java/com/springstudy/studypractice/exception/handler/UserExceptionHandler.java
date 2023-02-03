package com.springstudy.studypractice.exception.handler;

import com.springstudy.studypractice.exception.UserAuthException;
import com.springstudy.studypractice.exception.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class UserExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(UserAuthException.class)
    protected ResponseEntity<ErrorResponseDto> userAuthExceptionHandler(
            UserAuthException e, HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        messages.add(e.getUserValidError().getMessage());

        log.error("Messages = {}", messages);

        return createResponseEntity(
                e.getUserValidError().getHttpStatus(), messages, request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> fieldValidationExceptionHandler(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        e.getFieldErrors().forEach((fieldError -> {
            messages.add(fieldError.getDefaultMessage());
        }));

        log.error("Messages = {}", messages);

        return createResponseEntity(HttpStatus.BAD_REQUEST, messages, request);
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
