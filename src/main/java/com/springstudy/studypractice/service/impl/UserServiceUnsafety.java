package com.springstudy.studypractice.service.impl;

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
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

//@Service
@RequiredArgsConstructor
public class UserServiceUnsafety implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long joinUser(@Valid final SignUpRequestDto signUpRequestDto) {
        User user = checkUsernameDuplicate(signUpRequestDto);

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Override
    public String signInUser(final SignInRequestDto signInRequestDto) {
        User user = usernameAndPasswordValidate(signInRequestDto, null);
//        return user.getId();
        return "";
    }

    @Override
    public UserInfoResponseDto userInfo(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserAuthException(UserValidError.USERNAME_NOT_FOUND));

        return user.toDto();
    }

    @Override
    public List<UserInfoResponseDto> allUsersInfo() {
        List<UserInfoResponseDto> allUsersInfo = new ArrayList<>();
        userRepository.findAll().forEach(user -> allUsersInfo.add(user.toDto()));

        return allUsersInfo;
    }

    @Override
    @Transactional
    public void deleteUser(final WithdrawRequestDto withdrawRequestDto) {
        User user = usernameAndPasswordValidate(null, withdrawRequestDto);
        userRepository.delete(user);
    }

    private User checkUsernameDuplicate(final SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAuthException(UserValidError.DUPLICATE_USERNAME);
                });

//        return User.of(signUpRequestDto);
        return new User();
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
                .orElseThrow(() -> new UserAuthException(UserValidError.INVALID_USERNAME_PASSWORD));

        if (!user.getPassword().equals(password)) {
            throw new UserAuthException(UserValidError.INVALID_USERNAME_PASSWORD);
        }

        return user;
    }
}
