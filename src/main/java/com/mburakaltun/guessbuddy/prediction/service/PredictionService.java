package com.mburakaltun.guessbuddy.prediction.service;

import com.mburakaltun.guessbuddy.common.util.StringUtility;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDTO;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetPredictions;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetPredictions;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public ResponseGetPredictions getPredictions(RequestGetPredictions requestGetPredictions) {
        int page = requestGetPredictions.getPage();
        int size = requestGetPredictions.getSize();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<PredictionEntity> predictionEntityPage = predictionJpaRepository.findAll(pageRequest);

        return ResponseGetPredictions.builder()
                .predictionDTOList(predictionEntityPage.stream()
                        .map(this::mapPredictionEntityToDTO)
                        .toList())
                .build();
    }

    private PredictionDTO mapPredictionEntityToDTO(PredictionEntity prediction) {
        return PredictionDTO.builder()
                .id(prediction.getId())
                .createdDate(formatDateTime(prediction.getCreatedDate()))
                .updatedDate(formatDateTime(prediction.getUpdatedDate()))
                .creatorUserId(prediction.getCreatorUserId())
                .title(prediction.getTitle())
                .description(prediction.getDescription())
                .build();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return StringUtility.EMPTY;
        }
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss"));
    }

}
