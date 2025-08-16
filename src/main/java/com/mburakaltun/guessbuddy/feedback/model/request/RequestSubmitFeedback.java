package com.mburakaltun.guessbuddy.feedback.model.request;

import com.mburakaltun.guessbuddy.common.annotation.CleanContent;
import com.mburakaltun.guessbuddy.feedback.model.enums.FeedbackCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestSubmitFeedback {
    @NotBlank(message = "{validation.feedback.content.notBlank}")
    @Size(max = 1000, message = "{validation.feedback.content.size}")
    @CleanContent(message = "{validation.feedback.content.filtered}")
    private String content;

    private FeedbackCategory category;
}
