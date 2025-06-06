package com.mburakaltun.guessbuddy.prediction.model.request;

import com.mburakaltun.guessbuddy.common.model.request.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class RequestGetUserPredictionRates extends PageableRequest {
}
