package com.mburakaltun.guessbuddy.requestlog.repository;

import com.mburakaltun.guessbuddy.requestlog.model.entity.RequestLogHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing request log history records.
 */
@Repository
public interface RequestLogHistoryRepository extends JpaRepository<RequestLogHistoryEntity, Long> {
}