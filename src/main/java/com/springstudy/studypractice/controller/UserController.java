package com.springstudy.studypractice.controller;

import com.springstudy.studypractice.controller.dto.SignInRequestDto;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.WithdrawalRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        log.info("Sign Up request = {}", signUpRequestDto);
        return null;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        log.info("Sign In request = {}", signInRequestDto);
        return null;
    }

    @GetMapping("")
    public ResponseEntity<Object> userInfo( // 파라미터로 username을 전달하면 해당 사용자의 정보, 만약 전달하지 않으면 모든 회원의 정보
            @RequestParam(required = false, defaultValue = "") String username) {
        log.info("User info request = {}", username);
        return null;
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(@RequestBody WithdrawalRequestDto withdrawalRequestDto) {
        log.warn("User withdrawal request = {}", withdrawalRequestDto);
        return null;
    }
}
