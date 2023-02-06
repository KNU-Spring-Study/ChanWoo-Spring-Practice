package com.springstudy.studypractice.service.impl;

import com.springstudy.studypractice.config.jwt.JwtProvider;
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
import org.springframework.lang.Nullable;
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

    @Override
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
    }

    @Override
    public UserInfoResponseDto userInfo(String username) {
        return null;
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
