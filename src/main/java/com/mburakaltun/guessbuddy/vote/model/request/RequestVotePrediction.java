package com.mburakaltun.guessbuddy.vote.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestVotePrediction {
    @NotNull
    private Long predictionId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;
}
