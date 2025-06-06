package com.mburakaltun.guessbuddy.vote.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestVotePrediction {
    @NotNull(message = "{validation.predictionId.notNull}")
    private Long predictionId;

    @NotNull(message = "{validation.score.notNull}")
    @Min(value = 1, message = "{validation.score.min}")
    @Max(value = 5, message = "{validation.score.max}")
    private Integer score;
}
