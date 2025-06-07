package com.mburakaltun.guessbuddy.authentication.repository;

import com.mburakaltun.guessbuddy.authentication.model.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByUserEntity_Id(Long userEntityId);
}
