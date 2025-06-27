package com.mburakaltun.guessbuddy.feedback.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseEntity;
import com.mburakaltun.guessbuddy.common.model.entity.BaseStatusEntity;
import com.mburakaltun.guessbuddy.feedback.model.converter.FeedbackCategoryConverter;
import com.mburakaltun.guessbuddy.feedback.model.enums.FeedbackCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "feedback")
@SQLRestriction("status != -1")
public class FeedbackEntity extends BaseStatusEntity {
    @Column(nullable = false)
    private String content;

    @Convert(converter = FeedbackCategoryConverter.class)
    @Column(nullable = false)
    private FeedbackCategory category;

    @Column(nullable = false)
    private Long userId;
}
