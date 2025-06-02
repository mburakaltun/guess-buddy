package com.mburakaltun.guessbuddy.prediction.service;

import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PredictionService {

    private final PredictionJpaRepository predictionJpaRepository;

    public ResponseCreatePrediction createPrediction(RequestCreatePrediction requestCreatePrediction, String userId) {
        PredictionEntity predictionEntity = new PredictionEntity();
        predictionEntity.setTitle(requestCreatePrediction.getTitle());
        predictionEntity.setDescription(requestCreatePrediction.getDescription());
        predictionEntity.setCreatorUserId(Long.parseLong(userId));
        PredictionEntity savedEntity = predictionJpaRepository.save(predictionEntity);

        return ResponseCreatePrediction.builder()
                .predictionId(savedEntity.getId())
                .build();
    }

}
