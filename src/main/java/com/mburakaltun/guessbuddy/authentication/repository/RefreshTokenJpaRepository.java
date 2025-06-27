package com.mburakaltun.guessbuddy.authentication.repository;

import com.mburakaltun.guessbuddy.authentication.model.entity.RefreshTokenEntity;
import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    List<RefreshTokenEntity> findAllByUser(UserEntity user);
    void deleteByToken(String token);
    void deleteAllByUser(UserEntity user);
}