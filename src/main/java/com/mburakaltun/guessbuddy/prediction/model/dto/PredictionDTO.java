package com.mburakaltun.guessbuddy.prediction.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PredictionDTO {
    private Long id;
    private String createdDate;
    private String updatedDate;
    private long creatorUserId;
    private String title;
    private String description;
    private double averageScore;
    private long voteCount;
}
