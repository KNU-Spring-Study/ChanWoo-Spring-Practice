package com.springstudy.studypractice.config.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    @DisplayName("토큰 발급")
    void jwtProvide() throws Exception {
        //given
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");

        //when
        String token = jwtProvider.createToken(1000L, "woopaca", roles);

        //then
        log.info("token = {}", token);
    }
}