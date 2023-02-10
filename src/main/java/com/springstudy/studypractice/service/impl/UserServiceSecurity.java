package com.springstudy.studypractice.service.impl;

import com.springstudy.studypractice.config.jwt.JwtProvider;
import com.springstudy.studypractice.config.jwt.JwtUtils;
import com.springstudy.studypractice.controller.dto.SignInRequestDto;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.UserInfoResponseDto;
import com.springstudy.studypractice.controller.dto.WithdrawRequestDto;
import com.springstudy.studypractice.entity.User;
import com.springstudy.studypractice.exception.UserAuthException;
import com.springstudy.studypractice.exception.error.UserValidError;
import com.springstudy.studypractice.repository.UserRepository;
import com.springstudy.studypractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceSecurity implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final String TOKEN_PREFIX = "Bearer ";

    @Override
    @Transactional
    public Long joinUser(SignUpRequestDto signUpRequestDto) {
        User user = checkUsernameDuplicate(signUpRequestDto);

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    private User checkUsernameDuplicate(final SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAuthException(UserValidError.DUPLICATE_USERNAME);
                });

        return User.of(signUpRequestDto, passwordEncoder);
    }


    /*@Override
    public String signInUser(SignInRequestDto signInRequestDto) {
        User user = usernameAndPasswordValidate(signInRequestDto, null);

        String token = jwtProvider.createToken(user.getId(), user.getUsername(), user.getRoles());
        return TOKEN_PREFIX + token;
    }

    private User usernameAndPasswordValidate(@Nullable final SignInRequestDto signInRequestDto,
                                             @Nullable final WithdrawRequestDto withdrawRequestDto) {
        String username = null;
        String password = null;

        if (signInRequestDto != null) {
            username = signInRequestDto.getUsername();
            password = signInRequestDto.getPassword();
        }
        if (withdrawRequestDto != null) {
            username = withdrawRequestDto.getUsername();
            password = withdrawRequestDto.getPassword();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserAuthException(UserValidError.USERNAME_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserAuthException(UserValidError.INVALID_PASSWORD);
        }

        return user;
    }*/

    @Override
    public String signInUser(SignInRequestDto signInRequestDto) {
        String username = signInRequestDto.getUsername();
        String password = signInRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new UserAuthException(UserValidError.INVALID_USERNAME_PASSWORD);
        }

        User user = null;
        if (authentication.isAuthenticated()) {
            user = (User) authentication.getPrincipal();
        }

        assert user != null;
        String token = jwtProvider.createToken(user.getId(), user.getUsername(), user.getRoles());
        return TOKEN_PREFIX + token;
    }


    @Override
    public UserInfoResponseDto userInfo(String authorization) {
        String token = authorization.split(" ")[1];
        Object principal = jwtUtils.getAuthentication(token).getPrincipal();
        User user = (User) principal;
        return user.toDto();
    }

    @Override
    public List<UserInfoResponseDto> allUsersInfo() {
        return null;
    }

    @Override
    @Transactional
    public void deleteUser(WithdrawRequestDto withdrawRequestDto) {

    }
}
