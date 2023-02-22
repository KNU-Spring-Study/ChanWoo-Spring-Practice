package com.springstudy.studypractice.exception.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springstudy.studypractice.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("AuthenticationException = {}", authException.getMessage());
        List<String> messages = new ArrayList<>();
        messages.add("토큰이 유효하지 않습니다.");

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorType(HttpStatus.UNAUTHORIZED.name())
                .messages(messages)
                .path(request.getRequestURI())
                .build();

        response.setStatus(errorResponseDto.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        response.getWriter().write(mapper.writeValueAsString(errorResponseDto));
    }
}
