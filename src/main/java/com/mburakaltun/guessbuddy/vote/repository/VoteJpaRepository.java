package com.mburakaltun.guessbuddy.vote.repository;

import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteJpaRepository extends JpaRepository<VoteEntity, Long> {
}
