package com.springstudy.studypractice.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    @Builder.Default
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private Integer status;
    private String errorType;
    private List<String> messages;
    private String path;
}
