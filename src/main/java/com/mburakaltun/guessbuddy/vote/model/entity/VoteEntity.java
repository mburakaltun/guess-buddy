package com.mburakaltun.guessbuddy.vote.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseStatusEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "vote")
@SQLRestriction("status != -1")
public class VoteEntity extends BaseStatusEntity {
    private long predictionId;
    private long voterUserId;
    private long roomId;
    private int score;
}
