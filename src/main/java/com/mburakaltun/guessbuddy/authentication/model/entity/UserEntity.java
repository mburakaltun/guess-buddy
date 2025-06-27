package com.mburakaltun.guessbuddy.authentication.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseStatusEntity;
import com.mburakaltun.guessbuddy.common.model.enums.AuthorizationRole;
import com.mburakaltun.guessbuddy.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "app_user")
@SQLRestriction("status != -1")
public class UserEntity extends BaseStatusEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String encodedPassword;

    @Enumerated(EnumType.STRING)
    private AuthorizationRole role;
}
