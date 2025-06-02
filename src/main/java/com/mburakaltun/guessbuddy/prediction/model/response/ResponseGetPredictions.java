package com.mburakaltun.guessbuddy.prediction.model.response;

import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseGetPredictions {
    private List<PredictionDTO> predictionDTOList;
}
