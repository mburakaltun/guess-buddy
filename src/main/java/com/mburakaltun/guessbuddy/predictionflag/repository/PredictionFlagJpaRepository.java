package com.mburakaltun.guessbuddy.predictionflag.repository;


import com.mburakaltun.guessbuddy.predictionflag.model.entity.PredictionFlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictionFlagJpaRepository extends JpaRepository<PredictionFlagEntity, Long> {
    List<PredictionFlagEntity> findByPredictionId(Long predictionId);
    Optional<PredictionFlagEntity> findByPredictionIdAndReporterUserId(Long predictionId, Long reporterUserId);
    long countByPredictionId(Long predictionId);
}