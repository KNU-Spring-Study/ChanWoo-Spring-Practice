package com.springstudy.studypractice.exception;

import com.springstudy.studypractice.exception.error.UserValidError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthException extends RuntimeException {

    private UserValidError userValidError;
}
