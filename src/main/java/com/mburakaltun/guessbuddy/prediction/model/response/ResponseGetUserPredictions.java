package com.mburakaltun.guessbuddy.prediction.model.response;

import com.mburakaltun.guessbuddy.common.model.response.PageableResponse;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGetUserPredictions extends PageableResponse {
    private List<PredictionDto> predictionDtoList;
}
