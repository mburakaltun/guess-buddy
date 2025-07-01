package com.mburakaltun.guessbuddy.prediction.repository;

import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionFlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionFlagJpaRepository extends JpaRepository<PredictionFlagEntity, Long> {
    List<PredictionFlagEntity> findByPredictionId(Long predictionId);
    Optional<PredictionFlagEntity> findByPredictionIdAndReporterUserId(Long predictionId, Long reporterUserId);
    List<PredictionFlagEntity> findByResolvedFalse();
    long countByPredictionId(Long predictionId);
    long countByPredictionIdAndResolvedFalse(Long predictionId);
}