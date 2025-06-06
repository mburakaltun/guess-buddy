package com.mburakaltun.guessbuddy.prediction.model.entity;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.common.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prediction")
public class PredictionEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    private UserEntity creatorUser;

    private String title;
    private String description;
    private long voteCount;
    private long totalScore;
}
