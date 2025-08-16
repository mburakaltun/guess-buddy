package com.mburakaltun.guessbuddy.prediction.model.request;

import com.mburakaltun.guessbuddy.common.annotation.CleanContent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestFlagPrediction {
    
    @NotNull(message = "{validation.predictionId.notNull}")
    private Long predictionId;
    
    @Size(max = 1000, message = "{validation.reason.size}")
    @CleanContent(message = "{validation.reason.filtered}")
    private String reason;
}