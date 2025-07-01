package com.mburakaltun.guessbuddy.prediction.service;

import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.service.ContentFilterService;
import com.mburakaltun.guessbuddy.common.util.StringUtility;
import com.mburakaltun.guessbuddy.feedback.model.enums.FeedbackCategory;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionFlagEntity;
import com.mburakaltun.guessbuddy.prediction.model.enums.PredictionErrorCode;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestFlagPrediction;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseFlagPrediction;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionFlagJpaRepository;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PredictionFlagService {

    private final PredictionFlagJpaRepository predictionFlagJpaRepository;
    private final PredictionJpaRepository predictionJpaRepository;
    private final ContentFilterService contentFilterService;

    @Transactional
    public ResponseFlagPrediction flagPrediction(RequestFlagPrediction request, Long reporterUserId) throws AppException {
        Optional<PredictionEntity> predictionEntityOptional = predictionJpaRepository.findById(request.getPredictionId());
        if (predictionEntityOptional.isEmpty()) {
            throw new AppException(PredictionErrorCode.PREDICTION_NOT_FOUND);
        }

        Optional<PredictionFlagEntity> existingFlag = predictionFlagJpaRepository.findByPredictionIdAndReporterUserId(request.getPredictionId(), reporterUserId);
        
        if (existingFlag.isPresent()) {
            throw new AppException(PredictionErrorCode.PREDICTION_ALREADY_FLAGGED);
        }

        contentFilterService.validateContent(request.getReason());

        PredictionFlagEntity flagEntity = new PredictionFlagEntity();
        flagEntity.setPredictionId(request.getPredictionId());
        flagEntity.setReporterUserId(reporterUserId);
        flagEntity.setReason(request.getReason());
        flagEntity.setResolved(false);

        PredictionFlagEntity savedFlag = predictionFlagJpaRepository.save(flagEntity);

        return ResponseFlagPrediction.builder()
                .flagId(savedFlag.getId())
                .predictionId(savedFlag.getPredictionId())
                .reporterUserId(savedFlag.getReporterUserId())
                .build();
    }
}