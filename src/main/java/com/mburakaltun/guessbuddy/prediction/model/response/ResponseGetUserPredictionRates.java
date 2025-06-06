package com.mburakaltun.guessbuddy.prediction.model.response;

import com.mburakaltun.guessbuddy.prediction.model.dto.UserPredictionHitRateDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseGetUserPredictionRates {
    private List<UserPredictionHitRateDto> userPredictionHitRateDtoList;
}
