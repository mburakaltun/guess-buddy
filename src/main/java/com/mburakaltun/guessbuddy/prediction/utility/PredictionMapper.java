package com.mburakaltun.guessbuddy.prediction.utility;

import com.mburakaltun.guessbuddy.common.util.StringUtility;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDto;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionScoreCountDto;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Map;

@UtilityClass
public class PredictionMapper {

    public PredictionDto toDto(PredictionEntity predictionEntity, Map<Long, Integer> userVotesMap, Map<Long, PredictionScoreCountDto> predictionScoreCountMap) {
        int userScore = userVotesMap.getOrDefault(predictionEntity.getId(), 0);
        PredictionScoreCountDto scoreCount = predictionScoreCountMap.get(predictionEntity.getId());

        return PredictionDto.builder()
                .id(predictionEntity.getId())
                .createdDate(formatDateTime(predictionEntity.getCreatedDate()))
                .updatedDate(formatDateTime(predictionEntity.getUpdatedDate()))
                .creatorUserId(predictionEntity.getCreatorUser().getId())
                .creatorUsername(predictionEntity.getCreatorUser().getUsername())
                .title(predictionEntity.getTitle())
                .description(predictionEntity.getDescription())
                .voteCount(scoreCount != null ? scoreCount.getVoteCount() : 0)
                .averageScore(scoreCount != null ? scoreCount.getAverageScore() : 0.0)
                .userScore(userScore)
                .build();
    }

    public PredictionDto toDto(PredictionEntity predictionEntity, Map<Long, PredictionScoreCountDto> predictionScoreCountMap) {
        PredictionScoreCountDto scoreCount = predictionScoreCountMap.get(predictionEntity.getId());

        return PredictionDto.builder()
                .id(predictionEntity.getId())
                .createdDate(formatDateTime(predictionEntity.getCreatedDate()))
                .updatedDate(formatDateTime(predictionEntity.getUpdatedDate()))
                .creatorUserId(predictionEntity.getCreatorUser().getId())
                .creatorUsername(predictionEntity.getCreatorUser().getUsername())
                .title(predictionEntity.getTitle())
                .description(predictionEntity.getDescription())
                .voteCount(scoreCount != null ? scoreCount.getVoteCount() : 0)
                .averageScore(scoreCount != null ? scoreCount.getAverageScore() : 0.0)
                .userScore(0)
                .build();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return StringUtility.EMPTY;
        }
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss"));
    }
}