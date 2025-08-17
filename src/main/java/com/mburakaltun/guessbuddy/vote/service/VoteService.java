package com.mburakaltun.guessbuddy.vote.service;

import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.prediction.constants.PredictionCacheNames;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionScoreCountDto;
import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import com.mburakaltun.guessbuddy.vote.model.request.RequestVotePrediction;
import com.mburakaltun.guessbuddy.vote.repository.VoteJpaRepository;
import com.mburakaltun.guessbuddy.vote.response.ResponseVotePrediction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final VoteJpaRepository voteJpaRepository;

    @Transactional
    @CacheEvict(cacheNames = {PredictionCacheNames.PREDICTIONS, PredictionCacheNames.USER_PREDICTIONS, PredictionCacheNames.PREDICTION_RATES}, allEntries = true)
    public ResponseVotePrediction createVote(RequestVotePrediction requestVotePrediction, Long userId, Long roomId) throws AppException {
        Long predictionId = requestVotePrediction.getPredictionId();

        Optional<VoteEntity> existingVoteOptional = voteJpaRepository.findByPredictionIdAndVoterUserId(predictionId, userId);

        if (existingVoteOptional.isPresent()) {
            VoteEntity existingVoteEntity = existingVoteOptional.get();
            existingVoteEntity.setScore(requestVotePrediction.getScore());
            voteJpaRepository.save(existingVoteEntity);
        } else {
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setPredictionId(requestVotePrediction.getPredictionId());
            voteEntity.setVoterUserId(userId);
            voteEntity.setRoomId(roomId);
            voteEntity.setScore(requestVotePrediction.getScore());
            voteJpaRepository.save(voteEntity);
        }

        PredictionScoreCountDto predictionScoreCountDto = voteJpaRepository.findScoreCountByPredictionId(predictionId);

        return ResponseVotePrediction.builder()
                .isVotedSuccessfully(true)
                .averageScore(predictionScoreCountDto.getAverageScore())
                .voteCount(predictionScoreCountDto.getVoteCount())
                .build();
    }
}
