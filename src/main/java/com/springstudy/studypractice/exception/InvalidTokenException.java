package com.springstudy.studypractice.exception;

import com.springstudy.studypractice.exception.error.TokenValidError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidTokenException extends RuntimeException {

    private TokenValidError tokenValidError;
}
