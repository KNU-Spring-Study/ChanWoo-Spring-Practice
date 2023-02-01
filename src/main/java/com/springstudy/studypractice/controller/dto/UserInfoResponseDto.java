package com.springstudy.studypractice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UserInfoResponseDto {

    private String username;
    private String email;
    private String phone;
    private int age;
}
