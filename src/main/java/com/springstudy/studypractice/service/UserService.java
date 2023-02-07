package com.springstudy.studypractice.service;

import com.springstudy.studypractice.controller.dto.SignInRequestDto;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.UserInfoResponseDto;
import com.springstudy.studypractice.controller.dto.WithdrawRequestDto;

import java.util.List;

public interface UserService {

    Long joinUser(final SignUpRequestDto signUpRequestDto);

    String signInUser(final SignInRequestDto signInRequestDto);

    UserInfoResponseDto userInfo(final String user);

    List<UserInfoResponseDto> allUsersInfo();

    void deleteUser(final WithdrawRequestDto withdrawRequestDto);
}
