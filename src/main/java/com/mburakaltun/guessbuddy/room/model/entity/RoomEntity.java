package com.mburakaltun.guessbuddy.room.model.entity;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.common.model.entity.BaseStatusEntity;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "room")
@SQLRestriction("status != -1")
public class RoomEntity extends BaseStatusEntity {
    @Column(nullable = false)
    private Long creatorUserId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String passcode;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PredictionEntity> predictions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "room_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> users = new HashSet<>();
}
