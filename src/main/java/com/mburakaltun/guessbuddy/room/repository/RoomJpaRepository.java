package com.mburakaltun.guessbuddy.room.repository;

import com.mburakaltun.guessbuddy.room.model.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, Long> {
    boolean existsByPasscode(String passcode);

    Optional<RoomEntity> findByPasscode(String passcode);

    List<RoomEntity> findByCreatorUserId(Long creatorUserId);
}
