package com.springstudy.studypractice.exception;

import com.springstudy.studypractice.exception.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class UserExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(UserAuthException.class)
    protected ResponseEntity<ErrorResponseDto> userAuthExceptionHandler(UserAuthException e, HttpServletRequest request) {
        log.error("Message = {}", e.getUserValidError().getMessage());

        String requestURI = request.getRequestURI();
        HttpStatus httpStatus = e.getUserValidError().getHttpStatus();

        return ResponseEntity.status(httpStatus)
                .body(ErrorResponseDto.builder()
                        .status(httpStatus.value())
                        .errorType(httpStatus.name())
                        .message(e.getUserValidError().getMessage())
                        .path(requestURI)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> fieldValidationExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Message = {}", e.getFieldError().getDefaultMessage());

        String requestURI = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus)
                .body(ErrorResponseDto.builder()
                        .status(httpStatus.value())
                        .errorType(httpStatus.name())
                        .message(e.getFieldError().getDefaultMessage())
                        .path(requestURI)
                        .build());
    }
}
