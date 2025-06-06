package com.mburakaltun.guessbuddy.prediction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserPredictionHitRateDto {
    private Long userId;
    private String username;
    private Long totalPredictionCount;
    private Long successfulPredictionCount;
    private Double successRate;
}
