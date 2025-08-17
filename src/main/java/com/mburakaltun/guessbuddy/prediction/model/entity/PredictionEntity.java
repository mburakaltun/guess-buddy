package com.mburakaltun.guessbuddy.prediction.model.entity;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.common.model.entity.BaseStatusEntity;
import com.mburakaltun.guessbuddy.room.model.entity.RoomEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "prediction")
@SQLRestriction("status != -1")
public class PredictionEntity extends BaseStatusEntity {
    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    private UserEntity creatorUser;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;
}
