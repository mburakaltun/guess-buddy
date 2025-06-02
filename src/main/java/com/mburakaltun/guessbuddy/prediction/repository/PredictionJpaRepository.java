package com.mburakaltun.guessbuddy.prediction.repository;

import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionJpaRepository extends JpaRepository<PredictionEntity, Long> {
}
