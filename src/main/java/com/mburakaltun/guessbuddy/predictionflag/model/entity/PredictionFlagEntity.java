package com.mburakaltun.guessbuddy.predictionflag.model.entity;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.common.model.entity.BaseEntity;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.predictionflag.model.enums.PredictionFlagReason;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prediction_flag",
        uniqueConstraints = @UniqueConstraint(columnNames = {"prediction_id", "reporter_user_id"}))
public class PredictionFlagEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "prediction_id", nullable = false)
    private PredictionEntity prediction;

    @ManyToOne
    @JoinColumn(name = "reporter_user_id", nullable = false)
    private UserEntity reporterUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PredictionFlagReason reason;

    @Column
    private String comment;
}