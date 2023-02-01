package com.springstudy.studypractice.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message = "회원 아이디는 비어있을 수 없습니다.")
    private String username;
    @NotBlank(message = "회원 비밀번호는 비어있을 수 없습니다.")
    @Size(min = 6, message = "회원 비밀번호는 6자 이상이어야 합니다.")
    private String password;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "연락처는 '000-0000-0000' 형식으로 입력해야 합니다.")
    private String phone;
    @Min(value = 14, message = "회원의 나이는 14세 이상이어야 합니다.")
    @Max(value = 120, message = "회원의 나이는 120세 이하여야 합니다.")
    private Integer age;

    // 테스트 용도. 배포 시 제거
    @Builder
    public SignUpRequestDto(String username, String password, String email, String phone, Integer age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.age = age;
    }
}
