package com.mburakaltun.guessbuddy.predictionflag.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.enums.Status;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.model.enums.PredictionErrorCode;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import com.mburakaltun.guessbuddy.predictionflag.model.entity.PredictionFlagEntity;
import com.mburakaltun.guessbuddy.predictionflag.model.enums.PredictionFlagErrorCode;
import com.mburakaltun.guessbuddy.predictionflag.model.request.RequestFlagPrediction;
import com.mburakaltun.guessbuddy.predictionflag.model.response.ResponseFlagPrediction;
import com.mburakaltun.guessbuddy.predictionflag.repository.PredictionFlagJpaRepository;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PredictionFlagService {

    private final PredictionFlagJpaRepository predictionFlagJpaRepository;
    private final PredictionJpaRepository predictionJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public ResponseFlagPrediction flagPrediction(RequestFlagPrediction request, Long userId) throws AppException {
        UserEntity reporter = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        PredictionEntity prediction = predictionJpaRepository.findById(request.getPredictionId())
                .orElseThrow(() -> new AppException(PredictionErrorCode.PREDICTION_NOT_FOUND));

        Optional<PredictionFlagEntity> existingFlag = predictionFlagJpaRepository
                .findByPredictionIdAndReporterUserId(prediction.getId(), userId);

        if (existingFlag.isPresent()) {
            throw new AppException(PredictionFlagErrorCode.PREDICTION_ALREADY_FLAGGED);
        }

        PredictionFlagEntity predictionFlagEntity = new PredictionFlagEntity();
        predictionFlagEntity.setPrediction(prediction);
        predictionFlagEntity.setReporterUser(reporter);
        predictionFlagEntity.setReason(request.getReason());
        predictionFlagEntity.setComment(request.getComment());

        PredictionFlagEntity savedFlag = predictionFlagJpaRepository.save(predictionFlagEntity);

        long flagCount = predictionFlagJpaRepository.countByPredictionId(prediction.getId());
        if (flagCount >= 5) {
            prediction.setStatus(Status.DELETED);
            predictionJpaRepository.save(prediction);
        }

        return ResponseFlagPrediction.builder()
                .flagId(savedFlag.getId())
                .build();
    }
}