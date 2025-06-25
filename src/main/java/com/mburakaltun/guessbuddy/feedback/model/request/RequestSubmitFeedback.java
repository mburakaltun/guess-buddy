package com.mburakaltun.guessbuddy.feedback.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestSubmitFeedback {
    @NotBlank(message = "{validation.feedback.content.notBlank}")
    @Size(max = 1000, message = "{validation.feedback.content.size}")
    private String content;

    private String category;
}
