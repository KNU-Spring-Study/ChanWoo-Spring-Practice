/*
package com.springstudy.studypractice.repository;

import com.springstudy.studypractice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CollectionUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        userRepository.reset();
    }

    @Test
    @DisplayName("회원 저장")
    void saveTest() throws Exception {
        //given
        User user = createUser("wooapca");

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void findByIdTest() throws Exception {
        //given
        User user = createUser("wooapca");
        User savedUser = userRepository.save(user);

        //when
        Optional<User> findUser = userRepository.findById(savedUser.getId());

        //then
        assertThat(findUser.get()).isEqualTo(savedUser);
    }

    @Test
    void findByUsernameTest() throws Exception {
        //given
        User user = createUser("woopaca");
        User savedUser = userRepository.save(user);

        //when
        Optional<User> findUser = userRepository.findByUsername("woopaca");

        //then
        assertThat(findUser.get()).isEqualTo(savedUser);
    }

    @Test
    void findAllTest() throws Exception {
        //given
        User user1 = createUser("wooapca");
        User user2 = createUser("zz");
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        List<User> users = userRepository.findAll();

        //then
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void deleteTest() throws Exception {
        //given
        User user = createUser("woopaca");
        User savedUser = userRepository.save(user);

        //when
        userRepository.delete(savedUser);

        //then
        Optional<User> optionalUser = userRepository.findById(savedUser.getId());
        assertThrows(NoSuchElementException.class, () -> optionalUser.get());
    }

    private User createUser(String username) {
        return User.builder()
                .username(username)
                .password("woopaca")
                .email("jcw001031@gmail.com")
                .phone("010-9517-1530")
                .age(24)
                .build();
    }
}*/
