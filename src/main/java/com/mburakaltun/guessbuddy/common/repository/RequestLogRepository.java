package com.mburakaltun.guessbuddy.common.repository;

import com.mburakaltun.guessbuddy.common.model.entity.RequestLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLogEntity, Long> {
}
