package com.mburakaltun.guessbuddy.vote.service;

import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import com.mburakaltun.guessbuddy.vote.model.request.RequestVotePrediction;
import com.mburakaltun.guessbuddy.vote.repository.VoteJpaRepository;
import com.mburakaltun.guessbuddy.vote.response.ResponseVotePrediction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final VoteJpaRepository voteJpaRepository;

    public ResponseVotePrediction votePrediction(@Valid RequestVotePrediction requestVotePrediction, String userId) {
        VoteEntity voteEntity = new VoteEntity();
        voteEntity.setPredictionId(requestVotePrediction.getPredictionId());
        voteEntity.setVoterUserId(Long.parseLong(userId));
        voteEntity.setScore(requestVotePrediction.getScore());
        VoteEntity savedVote = voteJpaRepository.save(voteEntity);

        return ResponseVotePrediction.builder()
                .voteId(savedVote.getId())
                .build();
    }
}
