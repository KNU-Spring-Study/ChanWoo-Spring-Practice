package com.springstudy.studypractice.repository;

import com.springstudy.studypractice.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DbUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("회원 저장")
    void userSaveTest() throws Exception {
        //given
        User user = createUser("woopaca");

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser).isSameAs(user);
    }

    @Test
    @Transactional
    @DisplayName("username으로 조회 실패")
    void findByUsernameTest() throws Exception {
        //given
        User user = createUser("woopaca");
        userRepository.save(user);

        //when
        Optional<User> optionalUser = userRepository.findByUsername("asdf");

        //then
        assertThrows(NoSuchElementException.class, () -> optionalUser.get());
    }

    @Test
    @Transactional
    @DisplayName("모든 회원 조회")
    void allUsersFindTest() throws Exception {
        //given
        User user1 = createUser("지찬우");
        User user2 = createUser("woopaca");
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        List<User> users = userRepository.findAll();

        //then
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    @DisplayName("회원 삭제 테스트")
    void userDeleteTest() throws Exception {
        //given
        User user = createUser("woopaca");
        User savedUser = userRepository.save(user);

        Long id = savedUser.getId();

        //when
        userRepository.delete(savedUser);
        Optional<User> optionalUser = userRepository.findById(id);

        //then
        assertThrows(NoSuchElementException.class, () -> optionalUser.get());
    }

    private User createUser(String username) {
        return User.builder()
                .username(username)
                .password("woopaca")
                .age(15)
                .build();
    }
}