package com.mburakaltun.guessbuddy.requestlog.model.entity;

import com.mburakaltun.guessbuddy.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "request_log_history")
public class RequestLogHistoryEntity extends BaseEntity {
    private Long responseTime;
    private Integer responseStatus;
    private String requestMethod;
    private String requestUrl;

    @Column(columnDefinition = "TEXT")
    private String requestHeaders;

    @Column(columnDefinition = "TEXT")
    private String requestPayload;

    @Column(columnDefinition = "TEXT")
    private String responsePayload;

    private String requestIp;
}