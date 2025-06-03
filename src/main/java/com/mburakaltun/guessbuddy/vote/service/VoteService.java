package com.mburakaltun.guessbuddy.vote.service;

import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.model.exception.PredictionErrorCode;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import com.mburakaltun.guessbuddy.vote.model.request.RequestVotePrediction;
import com.mburakaltun.guessbuddy.vote.repository.VoteJpaRepository;
import com.mburakaltun.guessbuddy.vote.response.ResponseVotePrediction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final VoteJpaRepository voteJpaRepository;
    private final PredictionJpaRepository predictionJpaRepository;

    public ResponseVotePrediction votePrediction(@Valid RequestVotePrediction requestVotePrediction, String userId) throws AppException {
        // Check if the prediction exists
        PredictionEntity predictionEntity = predictionJpaRepository.findById(requestVotePrediction.getPredictionId())
                .orElseThrow(() -> new AppException(PredictionErrorCode.PREDICTION_NOT_FOUND));

        Optional<VoteEntity> existingVoteOptional = voteJpaRepository.findByPredictionIdAndVoterUserId(requestVotePrediction.getPredictionId(), Long.parseLong(userId));
        if (existingVoteOptional.isPresent()) {
            // If the user has already voted, update the existing vote
            VoteEntity existingVoteEntity = existingVoteOptional.get();
            int oldScore = existingVoteEntity.getScore();
            existingVoteEntity.setScore(requestVotePrediction.getScore());
            voteJpaRepository.save(existingVoteEntity);

            predictionEntity.setTotalScore(predictionEntity.getTotalScore() - oldScore + requestVotePrediction.getScore());
        } else {
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setPredictionId(requestVotePrediction.getPredictionId());
            voteEntity.setVoterUserId(Long.parseLong(userId));
            voteEntity.setScore(requestVotePrediction.getScore());
            voteJpaRepository.save(voteEntity);

            predictionEntity.setVoteCount(predictionEntity.getVoteCount() + 1);
            predictionEntity.setTotalScore(predictionEntity.getTotalScore() + requestVotePrediction.getScore());
        }

        predictionJpaRepository.save(predictionEntity);

        return ResponseVotePrediction.builder()
                .isVotedSuccessfully(true)
                .build();
    }
}
