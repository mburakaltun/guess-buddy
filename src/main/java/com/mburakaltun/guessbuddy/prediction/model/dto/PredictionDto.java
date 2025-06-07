package com.mburakaltun.guessbuddy.prediction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String createdDate;
    private String updatedDate;
    private long creatorUserId;
    private String title;
    private String description;
    private double averageScore;
    private long voteCount;
    private int userScore;
    private String creatorUsername;
}
