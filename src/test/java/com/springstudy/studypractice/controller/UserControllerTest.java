package com.springstudy.studypractice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springstudy.studypractice.controller.dto.SignInRequestDto;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.WithdrawRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    public static final String URI_PREFIX = "/api/v1/users";

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("woopaca")
                .password("woopaca")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(URI_PREFIX + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(signUpRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("로그인 성공")
    void signInSuccess() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("jcw1031", "woopaca");

        //when
        ResultActions resultActions = mockMvc.perform(post(URI_PREFIX + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(signInRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 정보 조회 (파라미터 전달)")
    void specificUserInfoTest() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get(URI_PREFIX)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("username", "jcw1031"))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 정보 조회 (파라미터 전달 x)")
    void allUserInfoTest() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get(URI_PREFIX)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void userWithdrawalTest() throws Exception {
        //given
        WithdrawRequestDto withdrawRequestDto =
                new WithdrawRequestDto("jcw1031", "woopaca");

        //when
        ResultActions resultActions = mockMvc.perform(delete(URI_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(withdrawRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - Validation 에러")
    void signUpRequestDtoValidationError() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username(" ")
                .password("woopaca")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(URI_PREFIX + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(signUpRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 - username 중복")
    void signUpFailUsernameDuplicate() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("jcw1031")
                .password("woopaca")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(URI_PREFIX + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(signUpRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 실패 - username")
    void signInFailUsername() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("hello", "woopaca");

        //when
        ResultActions resultActions = mockMvc.perform(post(URI_PREFIX + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(signInRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password")
    void signInFailPasswordInvalid() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("jcw1031", "hello");

        //when
        ResultActions resultActions = mockMvc.perform(post(URI_PREFIX + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(signInRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("회원 정보 조회 실패 - username 없음")
    void userInfoFailUsernameNotFound() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get(URI_PREFIX)
                        .param("username", "hello")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - username")
    void withdrawUserFailUsername() throws Exception {
        //given
        WithdrawRequestDto withdrawRequestDto = new WithdrawRequestDto("hello", "woopaca");

        //when
        ResultActions resultActions = mockMvc.perform(delete(URI_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(withdrawRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - password")
    void withdrawUserFailPasswordInvalid() throws Exception {
        //given
        WithdrawRequestDto withdrawRequestDto = new WithdrawRequestDto("jcw1031", "hello");

        //when
        ResultActions resultActions = mockMvc.perform(delete(URI_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(withdrawRequestDto)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isUnauthorized());
    }
}