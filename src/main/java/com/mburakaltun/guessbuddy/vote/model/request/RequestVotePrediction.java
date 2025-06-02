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
    @Min(0)
    @Max(10)
    private Integer score;
}
