package com.mburakaltun.guessbuddy.prediction.model.response;

import com.mburakaltun.guessbuddy.common.model.response.PageableResponse;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ResponseGetPredictions extends PageableResponse {
    private List<PredictionDTO> predictionDTOList;
}
