package com.mburakaltun.guessbuddy.prediction.utility;

import com.mburakaltun.guessbuddy.common.util.StringUtility;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDTO;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Map;

@UtilityClass
public class PredictionMapper {

    public PredictionDTO toDTO(PredictionEntity predictionEntity, Map<Long, Integer> userVotesMap) {
        int userScore = userVotesMap.getOrDefault(predictionEntity.getId(), 0);

        return PredictionDTO.builder()
                .id(predictionEntity.getId())
                .createdDate(formatDateTime(predictionEntity.getCreatedDate()))
                .updatedDate(formatDateTime(predictionEntity.getUpdatedDate()))
                .creatorUserId(predictionEntity.getCreatorUser().getId())
                .creatorUsername(predictionEntity.getCreatorUser().getUsername())
                .title(predictionEntity.getTitle())
                .description(predictionEntity.getDescription())
                .voteCount(predictionEntity.getVoteCount())
                .averageScore(getAverageScore(predictionEntity))
                .userScore(userScore)
                .build();
    }

    private double getAverageScore(PredictionEntity predictionEntity) {
        if (predictionEntity.getVoteCount() == 0) {
            return 0.0;
        }
        return (double) predictionEntity.getTotalScore() / predictionEntity.getVoteCount();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return StringUtility.EMPTY;
        }
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss"));
    }
}
