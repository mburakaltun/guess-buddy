package com.mburakaltun.guessbuddy.prediction.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseStatusEntity;
import com.mburakaltun.guessbuddy.feedback.model.converter.FeedbackCategoryConverter;
import com.mburakaltun.guessbuddy.feedback.model.enums.FeedbackCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "prediction_flag")
@SQLRestriction("status != -1")
public class PredictionFlagEntity extends BaseStatusEntity {
    
    @Column(nullable = false)
    private Long predictionId;
    
    @Column(nullable = false)
    private Long reporterUserId;
    
    @Column(length = 1000)
    private String reason;
    
    @Column(nullable = false)
    private Boolean resolved = false;
    
    @Column(length = 500)
    private String adminNotes;
}