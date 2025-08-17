package com.mburakaltun.guessbuddy.requestlog.repository;

import com.mburakaltun.guessbuddy.requestlog.model.entity.RequestLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLogEntity, Long> {
}
