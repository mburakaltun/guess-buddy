package com.mburakaltun.guessbuddy.prediction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PredictionScoreCountDto {
    private Long predictionId;
    private Double averageScore;
    private Long voteCount;
}
