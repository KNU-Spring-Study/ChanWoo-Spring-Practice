package com.springstudy.studypractice.config;

import com.springstudy.studypractice.config.jwt.JwtAuthenticationFilter;
import com.springstudy.studypractice.exception.handler.auth.CustomAccessDeniedHandler;
import com.springstudy.studypractice.exception.handler.auth.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean // 모든 설정은 전달받은 HttpSecurity에 설정
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.httpBasic().disable() // UI를 사용하는 것을 기본값으로 가진 시큐리티 설정을 비활성화
                .csrf().disable() // REST API에서는 CSRF 보안이 불필요 -> 비활성화 (CSRF = Cross-Site Request Forgery)
                //REST API 기반 애플리케이션의 동작 방식을 설정. 세션을 사용하지 않고 JWT로 인증을 처리하기 때문에 STATELESS로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // 애플리케이션에 들어오는 요청에 대한 사용 권한을 검증
                .antMatchers("/api/v1/users/sign-in", "/api/v1/users/sign-up").permitAll() // 해당 경로에 대해서는 모두에게 허용
                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .anyRequest().hasRole("ADMIN") // 기타 요청(및 경로)는 인증된 권한을 가진 사용자에게 허용
                .and()
                // 권한을 확인하는 과정에서 통과하지 못하는 예외가 발생할 경우, 예외를 전달 -> CustomAccessDeniedHandler
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                // 인증 과정에서 예외가 발생할 경우, 예외를 전달 -> CustomAuthenticationEntryPoint
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                // 어느 필터 앞에 이 필터를 추가할지 설정 -> UsernamePasswordAuthenticationFilter 앞에 JwtAuthenticationFilter 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
