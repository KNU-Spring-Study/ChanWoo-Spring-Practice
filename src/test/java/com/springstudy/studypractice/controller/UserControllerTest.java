package com.springstudy.studypractice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springstudy.studypractice.controller.dto.SignInRequestDto;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.WithdrawalRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
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
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공")
    void signInSuccess() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("woopaca", "woopaca");

        //when
        ResultActions resultActions = mockMvc.perform(post(URI_PREFIX + "/sign-up")
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
                        .param("username", "woopaca"))
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
        WithdrawalRequestDto withdrawalRequestDto =
                new WithdrawalRequestDto("woopaca", "woopaca");

        //when
        ResultActions resultActions = mockMvc.perform(delete(URI_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(withdrawalRequestDto)))
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
}