package com.mburakaltun.guessbuddy.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "request_log")
public class RequestLogEntity extends BaseEntity {
    private Long responseTime;
    private Integer responseStatus;
    private String requestMethod;
    private String requestUrl;

    @Column(columnDefinition = "TEXT")
    private String requestPayload;

    @Column(columnDefinition = "TEXT")
    private String responsePayload;

    private String requestIp;
}
