package com.mburakaltun.guessbuddy.prediction.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCreatePrediction {
    private Long predictionId;
}
