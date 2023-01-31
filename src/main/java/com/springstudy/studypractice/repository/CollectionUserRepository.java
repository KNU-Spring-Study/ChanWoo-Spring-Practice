package com.springstudy.studypractice.repository;

import com.springstudy.studypractice.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CollectionUserRepository implements UserRepository{

    // HashMap은 Thread-Safety하지 않기 때문에 동시성 문제 발생 가능, concurrentHashMap은 이를 보완
    private final Map<Long, User> store = new ConcurrentHashMap<>();

    private Long id = 0L;

    @Override
    public User save(User user) {
        user.setId(++id);
        store.put(user.getId(), user);
        return store.get(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Set<Long> keySet = store.keySet();

        for (Long key : keySet) {
            User user = store.get(key);
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        final List<User> users = new ArrayList<>();

        store.forEach((key, value) -> {
            users.add(value);
        });

        return users;
    }

    @Override
    public void delete(User user) {
        store.remove(user.getId());
    }

    // 테스트에서 사용하는 메서드
    @Override
    public void reset() {
        store.clear();
    }
}
