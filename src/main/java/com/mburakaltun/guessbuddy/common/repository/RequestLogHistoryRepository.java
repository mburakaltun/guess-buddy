package com.mburakaltun.guessbuddy.common.repository;

import com.mburakaltun.guessbuddy.common.model.entity.RequestLogHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing request log history records.
 */
@Repository
public interface RequestLogHistoryRepository extends JpaRepository<RequestLogHistoryEntity, Long> {
}