package com.mburakaltun.guessbuddy.predictionflag.model.request;

import com.mburakaltun.guessbuddy.predictionflag.model.enums.PredictionFlagReason;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestFlagPrediction {
    @NotNull(message = "{validation.predictionId.notNull}")
    private Long predictionId;

    @NotNull(message = "{validation.predictionFlagReason.notNull}")
    private PredictionFlagReason reason;

    private String comment;
}
