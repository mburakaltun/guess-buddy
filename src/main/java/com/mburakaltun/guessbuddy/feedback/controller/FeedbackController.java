package com.mburakaltun.guessbuddy.feedback.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.feedback.model.request.RequestSubmitFeedback;
import com.mburakaltun.guessbuddy.feedback.model.response.ResponseSubmitFeedback;
import com.mburakaltun.guessbuddy.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feedback")
public class FeedbackController extends BaseController {

    private final FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<ResponseSubmitFeedback>> submitFeedback(@RequestHeader(AppHeaders.X_USER_ID) String userId,
                                                                              @RequestBody RequestSubmitFeedback requestSubmitFeedback) {
        ResponseSubmitFeedback response = feedbackService.submitFeedback(requestSubmitFeedback, Long.valueOf(userId));
        return ResponseEntity.ok(respond(response));
    }
}
