package com.mburakaltun.guessbuddy.predictionflag.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.predictionflag.model.request.RequestFlagPrediction;
import com.mburakaltun.guessbuddy.predictionflag.model.response.ResponseFlagPrediction;
import com.mburakaltun.guessbuddy.predictionflag.service.PredictionFlagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/predictions/flags")
@RequiredArgsConstructor
public class PredictionFlagController extends BaseController {

    private final PredictionFlagService predictionFlagService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResponseFlagPrediction>> flagPrediction(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                              @RequestBody @Valid RequestFlagPrediction request) throws AppException {
        ResponseFlagPrediction response = predictionFlagService.flagPrediction(request, userId);
        return ResponseEntity.ok(respond(response));
    }
}