package com.mburakaltun.guessbuddy.prediction.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.authentication.repository.UserJpaRepository;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDTO;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetPredictions;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseCreatePrediction;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetPredictions;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import com.mburakaltun.guessbuddy.prediction.utility.PredictionMapper;
import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import com.mburakaltun.guessbuddy.vote.repository.VoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PredictionService {

    private final PredictionJpaRepository predictionJpaRepository;
    private final VoteJpaRepository voteJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public ResponseCreatePrediction createPrediction(RequestCreatePrediction requestCreatePrediction, String userId) throws AppException {
        Optional<UserEntity> userEntityOptional = userJpaRepository.findById(Long.parseLong(userId));
        if (userEntityOptional.isEmpty()) {
            throw new AppException(AuthenticationErrorCode.USER_NOT_FOUND);
        }

        PredictionEntity predictionEntity = new PredictionEntity();
        predictionEntity.setTitle(requestCreatePrediction.getTitle());
        predictionEntity.setDescription(requestCreatePrediction.getDescription());
        predictionEntity.setCreatorUser(userEntityOptional.get());
        PredictionEntity savedEntity = predictionJpaRepository.save(predictionEntity);

        return ResponseCreatePrediction.builder()
                .predictionId(savedEntity.getId())
                .build();
    }

    public ResponseGetPredictions getPredictions(RequestGetPredictions requestGetPredictions, Long userId) {
        int page = requestGetPredictions.getPage();
        int size = requestGetPredictions.getSize();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<PredictionEntity> predictionEntityPage = predictionJpaRepository.findAll(pageRequest);

        List<Long> predictionIds = predictionEntityPage.stream()
                .map(PredictionEntity::getId)
                .toList();

        Map<Long, Integer> userVotesMap = getUserVotesMap(predictionIds, userId);

        List<PredictionDTO> predictionDTOList = predictionEntityPage.stream()
                .map(entity -> PredictionMapper.toDTO(entity, userVotesMap))
                .toList();

        return ResponseGetPredictions.builder()
                .predictionDTOList(predictionDTOList)
                .totalElements(predictionEntityPage.getTotalElements())
                .totalPages(predictionEntityPage.getTotalPages())
                .number(predictionEntityPage.getNumber())
                .isLast(predictionEntityPage.isLast())
                .build();
    }

    private Map<Long, Integer> getUserVotesMap(List<Long> predictionIds, Long userId) {
        Map<Long, Integer> userVotesMap = new HashMap<>();
        if (userId != null && !CollectionUtils.isEmpty(predictionIds)) {
            List<VoteEntity> userVotes = voteJpaRepository.findByPredictionIdInAndVoterUserId(predictionIds, userId);
            userVotesMap = userVotes.stream().collect(Collectors.toMap(VoteEntity::getPredictionId, VoteEntity::getScore));
        }
        return userVotesMap;
    }

}
