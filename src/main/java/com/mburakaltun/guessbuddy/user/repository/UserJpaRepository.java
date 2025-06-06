package com.mburakaltun.guessbuddy.user.repository;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);
}
