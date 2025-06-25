package com.mburakaltun.guessbuddy.feedback.repository;

import com.mburakaltun.guessbuddy.feedback.model.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {
}
