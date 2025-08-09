package com.mburakaltun.guessbuddy.user.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseStatusEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "user_block")
@SQLRestriction("status != -1")
public class UserBlockEntity extends BaseStatusEntity {
    private Long blockerUserId;

    private Long blockedUserId;
}
