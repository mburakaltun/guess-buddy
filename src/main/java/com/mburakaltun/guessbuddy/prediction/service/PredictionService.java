package com.mburakaltun.guessbuddy.prediction.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.prediction.model.dto.UserPredictionHitRateDto;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetUserPredictionRates;
import com.mburakaltun.guessbuddy.prediction.model.request.RequestGetUserPredictions;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetUserPredictionRates;
import com.mburakaltun.guessbuddy.prediction.model.response.ResponseGetUserPredictions;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.prediction.model.dto.PredictionDto;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private static final String PREDICTIONS_CACHE = "predictions";
    private static final String USER_PREDICTIONS_CACHE = "userPredictions";
    private static final String PREDICTION_RATES_CACHE = "predictionRates";

    private final PredictionJpaRepository predictionJpaRepository;
    private final VoteJpaRepository voteJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @CacheEvict(cacheNames = {PREDICTIONS_CACHE, USER_PREDICTIONS_CACHE, PREDICTION_RATES_CACHE}, allEntries = true)
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

    @Cacheable(cacheNames = PREDICTIONS_CACHE, key = "'predictions_' + #userId + '_' + #requestGetPredictions.page + '_' + #requestGetPredictions.size")
    public ResponseGetPredictions getPredictions(RequestGetPredictions requestGetPredictions, Long userId) {
        int page = requestGetPredictions.getPage();
        int size = requestGetPredictions.getSize();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<PredictionEntity> predictionEntityPage = predictionJpaRepository.findAll(pageRequest);

        List<Long> predictionIds = predictionEntityPage.stream()
                .map(PredictionEntity::getId)
                .toList();

        Map<Long, Integer> userVotesMap = getUserVotesMap(predictionIds, userId);

        List<PredictionDto> predictionDtoList = predictionEntityPage.stream()
                .map(entity -> PredictionMapper.toDto(entity, userVotesMap))
                .toList();

        return ResponseGetPredictions.builder()
                .predictionDtoList(predictionDtoList)
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

    @Cacheable(cacheNames = PREDICTION_RATES_CACHE, key = "'prediction_rates_' + #requestGetUserPredictionRates.page + '_' + #requestGetUserPredictionRates.size")
    public ResponseGetUserPredictionRates getUserPredictionRates(RequestGetUserPredictionRates requestGetUserPredictionRates) {
        int page = requestGetUserPredictionRates.getPage();
        int size = requestGetUserPredictionRates.getSize();

        Pageable pageable = PageRequest.of(page, size);
        Page<UserPredictionHitRateDto> userPredictionHitRateDtoPage = predictionJpaRepository.findAllUsersByPredictionHitRate(pageable);

        return ResponseGetUserPredictionRates.builder()
                .userPredictionHitRateDtoList(userPredictionHitRateDtoPage.getContent())
                .totalElements(userPredictionHitRateDtoPage.getTotalElements())
                .totalPages(userPredictionHitRateDtoPage.getTotalPages())
                .number(userPredictionHitRateDtoPage.getNumber())
                .isLast(userPredictionHitRateDtoPage.isLast())
                .build();
    }

    @Cacheable(cacheNames = USER_PREDICTIONS_CACHE, key = "'user_predictions_' + #userId + '_' + #requestGetUserPredictions.page + '_' + #requestGetUserPredictions.size")
    public ResponseGetUserPredictions getUserPredictions(RequestGetUserPredictions requestGetUserPredictions, Long userId) {
        int page = requestGetUserPredictions.getPage();
        int size = requestGetUserPredictions.getSize();

        Pageable pageable = PageRequest.of(page, size);
        Page<PredictionEntity> predictionEntityPage = predictionJpaRepository.findByCreatorUserIdOrderByAverageScore(userId, pageable);

        List<PredictionDto> predictionDtoList = predictionEntityPage.stream()
                .map(PredictionMapper::toDto)
                .toList();

        return ResponseGetUserPredictions.builder()
                .predictionDtoList(predictionDtoList)
                .totalElements(predictionEntityPage.getTotalElements())
                .totalPages(predictionEntityPage.getTotalPages())
                .number(predictionEntityPage.getNumber())
                .isLast(predictionEntityPage.isLast())
                .build();
    }
}
