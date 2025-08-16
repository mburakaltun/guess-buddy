package com.mburakaltun.guessbuddy.user.repository;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.user.model.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT new com.mburakaltun.guessbuddy.user.model.dto.UserDto(u.id, u.username, u.email) " +
            "FROM UserEntity u " +
            "JOIN UserBlockEntity ub ON u.id = ub.blockedUserId " +
            "WHERE ub.blockerUserId = :blockerId AND ub.status = 1")
    List<UserDto> findBlockedUsersByBlockerId(@Param("blockerId") Long blockerId);
}
