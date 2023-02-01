package com.springstudy.studypractice.exception;

import com.springstudy.studypractice.exception.error.UserValidError;
import lombok.Getter;

@Getter
public class UserAuthException extends RuntimeException {

    private UserValidError userValidError;
}
