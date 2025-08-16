package com.mburakaltun.guessbuddy.prediction.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestFlagPrediction;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetPredictions;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetUserPredictionRates;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseFlagPrediction;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetPredictions;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetUserPredictionRates;
import com.mburakaltun.guessbuddy.prediction.service.PredictionFlagService;
import com.mburakaltun.guessbuddy.prediction.service.PredictionService;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetUserPredictions;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetUserPredictions;
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
@RequestMapping("/predictions")
public class PredictionController extends BaseController {

    private final PredictionService predictionService;
    private final PredictionFlagService predictionFlagService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResponseCreatePrediction>> createPrediction(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                                  @RequestHeader(AppHeaders.X_ROOM_ID) Long roomId,
                                                                                  @RequestBody @Valid RequestCreatePrediction requestCreatePrediction) throws AppException {
        ResponseCreatePrediction response = predictionService.createPrediction(requestCreatePrediction, userId, roomId);
        return new ResponseEntity<>(respond(response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ResponseGetPredictions>> getPredictions(@RequestHeader(value = AppHeaders.X_USER_ID) Long userId,
                                                                              @RequestHeader(AppHeaders.X_ROOM_ID) Long roomId,
                                                                              @ModelAttribute @Valid RequestGetPredictions requestGetPredictions) {
        ResponseGetPredictions response = predictionService.getPredictions(requestGetPredictions, userId, roomId);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }

    @GetMapping("/user-hit-rates")
    public ResponseEntity<ApiResponse<ResponseGetUserPredictionRates>> getUserPredictionRates(@RequestHeader(AppHeaders.X_ROOM_ID) Long roomId,
                                                                                              @ModelAttribute @Valid RequestGetUserPredictionRates requestGetUserPredictionRates) {
        ResponseGetUserPredictionRates response = predictionService.getUserPredictionRates(requestGetUserPredictionRates, roomId);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }

    @GetMapping("/my-predictions")
    public ResponseEntity<ApiResponse<ResponseGetUserPredictions>> getUserPredictions(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                                      @RequestHeader(AppHeaders.X_ROOM_ID) Long roomId,
                                                                                      @ModelAttribute @Valid RequestGetUserPredictions requestGetUserPredictions) throws AppException {
        ResponseGetUserPredictions response = predictionService.getUserPredictions(requestGetUserPredictions, userId, roomId);
        return ResponseEntity.ok(respond(response));
    }

    @PostMapping("/flag")
    public ResponseEntity<ApiResponse<ResponseFlagPrediction>> flagPrediction(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                              @RequestBody @Valid RequestFlagPrediction requestFlagPrediction) throws AppException {
        ResponseFlagPrediction response = predictionFlagService.flagPrediction(requestFlagPrediction, userId);
        return new ResponseEntity<>(respond(response), HttpStatus.CREATED);
    }
}
