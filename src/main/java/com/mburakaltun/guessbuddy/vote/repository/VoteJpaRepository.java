package com.mburakaltun.guessbuddy.vote.repository;

import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionScoreCountDto;
import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
                SELECT new com.mburakaltun.guessbuddy.prediction.model.dto.PredictionScoreCountDto(
                    v.predictionId,
                    AVG(v.score),
                    COUNT(v)
                )
                FROM VoteEntity v
                WHERE v.predictionId IN :predictionIds
                GROUP BY v.predictionId
            """)
    List<PredictionScoreCountDto> findScoreCountsByPredictionIds(List<Long> predictionIds);

    @Query("""
                SELECT new com.mburakaltun.guessbuddy.prediction.model.dto.PredictionScoreCountDto(
                    v.predictionId,
                    AVG(v.score),
                    COUNT(v)
                )
                FROM VoteEntity v
                WHERE v.predictionId = :predictionId
                GROUP BY v.predictionId
            """)
    PredictionScoreCountDto findScoreCountByPredictionId(Long predictionId);
}
