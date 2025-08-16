package com.mburakaltun.guessbuddy.vote.repository;

import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteJpaRepository extends JpaRepository<VoteEntity, Long> {
    Optional<VoteEntity> findByPredictionIdAndVoterUserId(long predictionId, long voterUserId);

    List<VoteEntity> findByPredictionIdInAndVoterUserId(List<Long> predictionIds, Long userIdLong);

    List<VoteEntity> findByVoterUserId(Long userId);

    List<VoteEntity> findByPredictionIdIn(List<Long> predictionIds);

    List<VoteEntity> findByVoterUserIdAndRoomId(Long userId, Long roomId);
}
