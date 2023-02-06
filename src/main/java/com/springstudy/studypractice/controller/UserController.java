package com.springstudy.studypractice.controller;

import com.springstudy.studypractice.controller.dto.SignInRequestDto;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.UserInfoResponseDto;
import com.springstudy.studypractice.controller.dto.WithdrawRequestDto;
import com.springstudy.studypractice.service.UserService;
import lombok.RequiredArgsConstructor;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid final SignUpRequestDto signUpRequestDto)
            throws URISyntaxException {
        log.info("Sign Up request = {}", signUpRequestDto);

        Long signUpUserId = userService.joinUser(signUpRequestDto);
        URI uri = new URI("/api/v1/users/" + signUpUserId);

        return ResponseEntity.created(uri).body("Sign-Up Success! User number = " + signUpUserId);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody final SignInRequestDto signInRequestDto) {
        log.info("Sign In request = {}", signInRequestDto);

        String token = userService.signInUser(signInRequestDto);

        return ResponseEntity.ok().header("Set-Cookie", "Authorization=" + token)
                .body("Sign-In Success!");
    }

    @GetMapping("") // 파라미터로 username을 전달하면 해당 사용자의 정보, 만약 전달하지 않으면 모든 회원의 정보
    public ResponseEntity<Object> userInfo(
            @RequestParam(required = false) final String username) {
        log.info("User info request = {}", username);

        if (username == null) {
            List<UserInfoResponseDto> userInfoList = userService.allUsersInfo();
            return ResponseEntity.ok().body(userInfoList);
        }

        UserInfoResponseDto userInfoResponseDto = userService.userInfo(username);
        return ResponseEntity.ok().body(userInfoResponseDto);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(@RequestBody final WithdrawRequestDto withdrawRequestDto) {
        log.warn("User withdrawal request = {}", withdrawRequestDto);

        userService.deleteUser(withdrawRequestDto);
        return ResponseEntity.ok().body("Membership Withdraw Success!");
    }
}
