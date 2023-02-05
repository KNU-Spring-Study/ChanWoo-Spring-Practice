package com.springstudy.studypractice.repository;

import com.springstudy.studypractice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class DbUserRepository implements UserRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public DbUserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username");

        Object findUser;
        try {
            findUser = query.setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            findUser = null;
        }
        User user = (User) findUser;
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
        return users;
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public void reset() {
        log.warn("This method is for testing purposes only.");
    }
}
