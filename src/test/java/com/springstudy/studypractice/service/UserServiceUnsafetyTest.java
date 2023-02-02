package com.springstudy.studypractice.service;

import com.springstudy.studypractice.controller.dto.SignInRequestDto;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.UserInfoResponseDto;
import com.springstudy.studypractice.controller.dto.WithdrawalRequestDto;
import com.springstudy.studypractice.entity.User;
import com.springstudy.studypractice.exception.UserAuthException;
import com.springstudy.studypractice.exception.error.UserValidError;
import com.springstudy.studypractice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceUnsafetyTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("woopaca")
                .password("woopaca")
                .build();

        //when
        Long userId = userService.joinUser(signUpRequestDto);
        User user = userRepository.findById(userId).orElse(null);

        //then
        assertThat(user.getUsername()).isEqualTo(signUpRequestDto.getUsername());
    }

    @Test
    @DisplayName("로그인 성공")
    void signInSuccess() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("jcw1031", "woopaca");

        //when
        Long userId = userService.signInUser(signInRequestDto);
        User user = userRepository.findById(userId).orElse(null);

        //then
        assertThat(user.getPassword()).isEqualTo(signInRequestDto.getPassword());
    }

    @Test
    @DisplayName("회원 정보 조회")
    void userInfoTest() throws Exception {
        //given
        User user = User.builder()
                .username("test")
                .password("testest")
                .build();
        userRepository.save(user);

        //when
        UserInfoResponseDto specificUserInfo = userService.userInfo("jcw1031");
        List<UserInfoResponseDto> allUsersInfo = userService.allUsersInfo();

        //then
        assertAll("User information inquiry",
                () -> assertThat(specificUserInfo.getUsername()).isEqualTo("jcw1031"),
                () -> assertThat(allUsersInfo.size()).isEqualTo(3));
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void withdrawalUserSuccess() throws Exception {
        //given
        User user = User.builder()
                .username("test")
                .password("testest")
                .build();
        userRepository.save(user);

        WithdrawalRequestDto withdrawalRequestDto =
                new WithdrawalRequestDto("test", "testest");

        //when
        userService.deleteUser(withdrawalRequestDto);

        //then
        assertThrows(UserAuthException.class, () -> userService.userInfo("test"));
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void signUpFailUsernameDuplicate() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("jcw1031")
                .password("woopaca")
                .build();

        //when
        UserAuthException userAuthException = assertThrows(UserAuthException.class, () ->
                userService.joinUser(signUpRequestDto));

        //then
        assertThat(userAuthException.getUserValidError().getMessage())
                .isEqualTo(UserValidError.DUPLICATE_USERNAME.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 아이디")
    void signInFailUsername() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("asdfsaf", "wooapca");

        //when
        UserAuthException userAuthException = assertThrows(UserAuthException.class, () ->
                userService.signInUser(signInRequestDto));

        //then
        assertThat(userAuthException.getUserValidError().getMessage())
                .isEqualTo(UserValidError.USERNAME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호")
    void signInFailPassword() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("jcw1031", "zzzzzz");

        //when
        UserAuthException userAuthException = assertThrows(UserAuthException.class, () ->
                userService.signInUser(signInRequestDto));

        //then
        assertThat(userAuthException.getUserValidError().getMessage())
                .isEqualTo(UserValidError.INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 실패")
    void withdrawalUserFail() throws Exception {
        //given
        WithdrawalRequestDto withdrawalRequestDto = new WithdrawalRequestDto("ewefsf", "woopaca");

        //when
        UserAuthException userAuthException = assertThrows(UserAuthException.class, () ->
                userService.deleteUser(withdrawalRequestDto));

        //then
        assertThat(userAuthException.getUserValidError().getMessage())
                .isEqualTo(UserValidError.USERNAME_NOT_FOUND.getMessage());
    }
}