package com.mburakaltun.guessbuddy.feedback.repository;

import com.mburakaltun.guessbuddy.feedback.model.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {
    List<FeedbackEntity> findByUserId(Long userId);
}
