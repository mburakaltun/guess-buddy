package com.mburakaltun.guessbuddy.vote.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.vote.model.request.RequestVotePrediction;
import com.mburakaltun.guessbuddy.vote.response.ResponseVotePrediction;
import com.mburakaltun.guessbuddy.vote.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vote")
public class VoteController extends BaseController {

    private final VoteService voteService;

    @PostMapping("/votePrediction")
    public ResponseEntity<ApiResponse<ResponseVotePrediction>> votePrediction(@RequestHeader(AppHeaders.X_USER_ID) String userId,
                                                                              @RequestBody @Valid RequestVotePrediction requestVotePrediction) throws AppException {
        ResponseVotePrediction response = voteService.votePrediction(requestVotePrediction, userId);
        return new ResponseEntity<>(respond(response), HttpStatus.CREATED);
    }
}
