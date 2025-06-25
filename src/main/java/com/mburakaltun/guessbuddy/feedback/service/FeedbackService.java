package com.mburakaltun.guessbuddy.feedback.service;

import com.mburakaltun.guessbuddy.feedback.model.entity.FeedbackEntity;
import com.mburakaltun.guessbuddy.feedback.model.request.RequestSubmitFeedback;
import com.mburakaltun.guessbuddy.feedback.model.response.ResponseSubmitFeedback;
import com.mburakaltun.guessbuddy.feedback.repository.FeedbackJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackJpaRepository feedbackJpaRepository;

    public ResponseSubmitFeedback submitFeedback(RequestSubmitFeedback requestSubmitFeedback, Long userId) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setUserId(userId);
        feedbackEntity.setContent(requestSubmitFeedback.getContent());
        feedbackEntity.setCategory(requestSubmitFeedback.getCategory());

        feedbackJpaRepository.save(feedbackEntity);

        return ResponseSubmitFeedback.builder()
                .build();
    }
}
