package com.springstudy.studypractice.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenValidError {

    AUTHORIZATION_HEADER_PREFIX_ERROR(HttpStatus.BAD_REQUEST,
            "Authorization 헤더는 Bearer로 시작해야 합니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");


    private final HttpStatus httpStatus;
    private final String message;
    }
