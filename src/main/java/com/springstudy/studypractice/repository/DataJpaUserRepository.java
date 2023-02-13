package com.springstudy.studypractice.repository;

import com.springstudy.studypractice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findByUsername(String username);
}
