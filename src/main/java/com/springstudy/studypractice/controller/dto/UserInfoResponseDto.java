package com.springstudy.studypractice.controller.dto;

import com.springstudy.studypractice.entity.Membership;
import lombok.Builder;
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
    private Integer age;
    private Membership membership;

    @Builder
    public UserInfoResponseDto(String username, String email, String phone,
                               Integer age, Membership membership) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.membership = membership;
    }
}
