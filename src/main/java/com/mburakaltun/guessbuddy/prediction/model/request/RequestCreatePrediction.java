package com.mburakaltun.guessbuddy.prediction.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreatePrediction {
    @NotNull(message = "Title cannot be null")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 31 characters")
    private String title;

    @NotNull(message = "Description cannot be null")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotNull(message = "Creator User ID cannot be null")
    private long creatorUserId;
}
