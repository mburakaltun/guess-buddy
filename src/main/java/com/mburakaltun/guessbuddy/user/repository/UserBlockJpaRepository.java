package com.mburakaltun.guessbuddy.user.repository;

import com.mburakaltun.guessbuddy.user.model.entity.UserBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBlockJpaRepository extends JpaRepository<UserBlockEntity, Long> {

    Optional<UserBlockEntity> findByBlockerUserIdAndBlockedUserId(Long blockerUserId, Long blockedUserId);
}
