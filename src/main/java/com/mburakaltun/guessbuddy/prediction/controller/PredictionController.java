package com.mburakaltun.guessbuddy.prediction.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetPredictions;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetPredictions;
import com.mburakaltun.guessbuddy.prediction.service.PredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/prediction")
public class PredictionController extends BaseController {

    private final PredictionService predictionService;

    @PostMapping("/createPrediction")
    public ResponseEntity<ApiResponse<ResponseCreatePrediction>> createQuote(@RequestHeader(AppHeaders.X_USER_ID) String userId,
                                                                             @RequestBody @Valid RequestCreatePrediction requestCreatePrediction) {
        ResponseCreatePrediction response = predictionService.createPrediction(requestCreatePrediction, userId);
        return new ResponseEntity<>(respond(response), HttpStatus.CREATED);
    }

    @GetMapping("/getPredictions")
    public ResponseEntity<ApiResponse<ResponseGetPredictions>> getPredictions(@ModelAttribute @Valid RequestGetPredictions requestGetPredictions) {
        ResponseGetPredictions response = predictionService.getPredictions(requestGetPredictions);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }
}
