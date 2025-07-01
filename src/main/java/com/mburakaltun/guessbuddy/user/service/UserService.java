package com.mburakaltun.guessbuddy.user.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.enums.Status;
import com.mburakaltun.guessbuddy.common.service.ContentFilterService;
import com.mburakaltun.guessbuddy.feedback.model.entity.FeedbackEntity;
import com.mburakaltun.guessbuddy.feedback.repository.FeedbackJpaRepository;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import com.mburakaltun.guessbuddy.user.constants.UserCacheNames;
import com.mburakaltun.guessbuddy.user.model.enums.UserErrorCode;
import com.mburakaltun.guessbuddy.user.model.request.RequestChangePassword;
import com.mburakaltun.guessbuddy.user.model.request.RequestChangeUsername;
import com.mburakaltun.guessbuddy.user.model.request.RequestGetUserProfile;
import com.mburakaltun.guessbuddy.user.model.response.ResponseChangePassword;
import com.mburakaltun.guessbuddy.user.model.response.ResponseChangeUsername;
import com.mburakaltun.guessbuddy.user.model.response.ResponseDeleteUser;
import com.mburakaltun.guessbuddy.user.model.response.ResponseGetUserProfile;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import com.mburakaltun.guessbuddy.vote.repository.VoteJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final VoteJpaRepository voteJpaRepository;
    private final PredictionJpaRepository predictionJpaRepository;
    private final FeedbackJpaRepository feedbackJpaRepository;
    private final ContentFilterService contentFilterService;

    @Cacheable(cacheNames = UserCacheNames.USER_PROFILE, key = "#userId")
    public ResponseGetUserProfile getUserProfile(RequestGetUserProfile requestGetUserProfile, Long userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        return ResponseGetUserProfile.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .build();
    }

    @Transactional
    @CacheEvict(cacheNames = UserCacheNames.USER_PROFILE, key = "#userId")
    public ResponseChangeUsername changeUsername(RequestChangeUsername request, Long userId) throws AppException {
        contentFilterService.validateContent(request.getNewUsername());

        UserEntity userEntity = userJpaRepository.findById(userId).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        String newUsername = request.getNewUsername();
        validateUsername(newUsername, userEntity.getId());

        userEntity.setUsername(newUsername);
        userJpaRepository.save(userEntity);

        return ResponseChangeUsername.builder()
                .id(userEntity.getId())
                .username(newUsername)
                .build();
    }

    public ResponseChangePassword changePassword(RequestChangePassword requestChangePassword, Long userId) throws AppException {
        if (!requestChangePassword.getNewPassword().equals(requestChangePassword.getConfirmNewPassword())) {
            throw new AppException(UserErrorCode.PASSWORD_MISMATCH);
        }

        UserEntity userEntity = userJpaRepository.findById(userId).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));
        boolean isPasswordCorrect = passwordEncoder.matches(requestChangePassword.getCurrentPassword(), userEntity.getEncodedPassword());

        if (!isPasswordCorrect) {
            throw new AppException(UserErrorCode.PASSWORD_INCORRECT);
        }

        userEntity.setEncodedPassword(passwordEncoder.encode(requestChangePassword.getNewPassword()));
        userJpaRepository.save(userEntity);

        return ResponseChangePassword.builder()
                .userId(userEntity.getId())
                .build();
    }

    @Transactional
    @CacheEvict(cacheNames = UserCacheNames.USER_PROFILE, key = "#userId")
    public ResponseDeleteUser deleteUser(Long userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(userId).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        deleteVotes(userId);
        deletePredictions(userId);
        deleteFeedbacks(userId);

        userEntity.setStatus(Status.DELETED);
        userJpaRepository.save(userEntity);

        return ResponseDeleteUser.builder()
                .userId(userId)
                .build();
    }

    private void deleteFeedbacks(Long userId) {
        List<FeedbackEntity> feedbackEntityList = feedbackJpaRepository.findByUserId(userId);
        feedbackEntityList.forEach(feedback -> feedback.setStatus(Status.DELETED));
        feedbackJpaRepository.saveAll(feedbackEntityList);
    }

    private void deletePredictions(Long userId) {
        List<PredictionEntity> predictionEntityList = predictionJpaRepository.findByCreatorUserId(userId);
        predictionEntityList.forEach(prediction -> prediction.setStatus(Status.DELETED));
        predictionJpaRepository.saveAll(predictionEntityList);
    }

    private void deleteVotes(Long userId) {
        List<VoteEntity> voteEntityList = voteJpaRepository.findByVoterUserId(userId);
        voteEntityList.forEach(vote -> vote.setStatus(Status.DELETED));
        voteJpaRepository.saveAll(voteEntityList);
    }

    private void validateUsername(String username, Long currentUserId) throws AppException {
        boolean usernameExists = userJpaRepository.findByUsername(username)
                .map(user -> !user.getId().equals(currentUserId))
                .orElse(false);

        if (usernameExists) {
            throw new AppException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }
}
