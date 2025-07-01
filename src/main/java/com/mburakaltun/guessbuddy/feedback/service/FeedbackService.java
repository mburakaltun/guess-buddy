package com.mburakaltun.guessbuddy.feedback.service;

import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.service.ContentFilterService;
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
    private final ContentFilterService contentFilterService;

    public ResponseSubmitFeedback submitFeedback(RequestSubmitFeedback requestSubmitFeedback, Long userId) throws AppException {
        contentFilterService.validateContent(requestSubmitFeedback.getContent());

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setUserId(userId);
        feedbackEntity.setContent(requestSubmitFeedback.getContent());
        feedbackEntity.setCategory(requestSubmitFeedback.getCategory());

        feedbackJpaRepository.save(feedbackEntity);

        return ResponseSubmitFeedback.builder()
                .userId(userId)
                .build();
    }
}
