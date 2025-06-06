package com.mburakaltun.guessbuddy.prediction.repository;

import com.mburakaltun.guessbuddy.prediction.model.dto.UserPredictionHitRateDto;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionJpaRepository extends JpaRepository<PredictionEntity, Long> {

    @Query("""
                SELECT new com.mburakaltun.guessbuddy.prediction.model.dto.UserPredictionHitRateDto(
                    p.creatorUser.id,
                    p.creatorUser.username,
                    COUNT(p),
                    SUM(CASE WHEN p.voteCount > 0 AND (p.totalScore * 1.0 / p.voteCount) >= 2.5 THEN 1 ELSE 0 END),
                    1.0 * SUM(CASE WHEN p.voteCount > 0 AND (p.totalScore * 1.0 / p.voteCount) >= 2.5 THEN 1 ELSE 0 END) / COUNT(p)
                )
                FROM PredictionEntity p
                GROUP BY p.creatorUser.id, p.creatorUser.username
                ORDER BY 1.0 * SUM(CASE WHEN p.voteCount > 0 AND (p.totalScore * 1.0 / p.voteCount) >= 2.5 THEN 1 ELSE 0 END) / COUNT(p) DESC
            """)
    Page<UserPredictionHitRateDto> findAllUsersByPredictionHitRate(Pageable pageable);

    @Query("""
            SELECT p FROM PredictionEntity p
            WHERE p.creatorUser.id = :userId
            AND p.voteCount > 0
            ORDER BY (CAST(p.totalScore AS float) / p.voteCount) DESC
            """)
    Page<PredictionEntity> findByCreatorUserIdOrderByAverageScore(
            @Param("userId") Long userId,
            Pageable pageable
    );

}
