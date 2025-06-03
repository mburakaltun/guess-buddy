package com.mburakaltun.guessbuddy.prediction.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prediction")
public class PredictionEntity extends BaseEntity {
    private long creatorUserId;
    private String title;
    private String description;
    private long voteCount;
    private long totalScore;
}
