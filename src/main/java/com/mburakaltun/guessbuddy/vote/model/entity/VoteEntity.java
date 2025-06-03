package com.mburakaltun.guessbuddy.vote.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vote")
public class VoteEntity extends BaseEntity {
    private long predictionId;
    private long voterUserId;
    private int score;
}
