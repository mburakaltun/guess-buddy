package com.mburakaltun.guessbuddy.user.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.enums.Status;
import com.mburakaltun.guessbuddy.feedback.model.entity.FeedbackEntity;
import com.mburakaltun.guessbuddy.feedback.repository.FeedbackJpaRepository;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import com.mburakaltun.guessbuddy.room.model.entity.RoomEntity;
import com.mburakaltun.guessbuddy.room.repository.RoomJpaRepository;
import com.mburakaltun.guessbuddy.user.constants.UserCacheNames;
import com.mburakaltun.guessbuddy.user.model.dto.UserDto;
import com.mburakaltun.guessbuddy.user.model.entity.UserBlockEntity;
import com.mburakaltun.guessbuddy.user.model.enums.UserErrorCode;
import com.mburakaltun.guessbuddy.user.model.request.RequestBlockUser;
import com.mburakaltun.guessbuddy.user.model.request.RequestChangePassword;
import com.mburakaltun.guessbuddy.user.model.request.RequestChangeUsername;
import com.mburakaltun.guessbuddy.user.model.request.RequestGetUserProfile;
import com.mburakaltun.guessbuddy.user.model.request.RequestUnblockUser;
import com.mburakaltun.guessbuddy.user.model.response.ResponseBlockUser;
import com.mburakaltun.guessbuddy.user.model.response.ResponseChangePassword;
import com.mburakaltun.guessbuddy.user.model.response.ResponseChangeUsername;
import com.mburakaltun.guessbuddy.user.model.response.ResponseDeleteUser;
import com.mburakaltun.guessbuddy.user.model.response.ResponseGetBlockedUsers;
import com.mburakaltun.guessbuddy.user.model.response.ResponseGetUserProfile;
import com.mburakaltun.guessbuddy.user.model.response.ResponseUnblockUser;
import com.mburakaltun.guessbuddy.user.repository.UserBlockJpaRepository;
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
    private final UserBlockJpaRepository userBlockJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final VoteJpaRepository voteJpaRepository;
    private final PredictionJpaRepository predictionJpaRepository;
    private final FeedbackJpaRepository feedbackJpaRepository;
    private final RoomJpaRepository roomJpaRepository;

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

    @Transactional
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
        deleteRooms(userId);

        userEntity.setStatus(Status.DELETED);
        userJpaRepository.save(userEntity);

        return ResponseDeleteUser.builder()
                .userId(userId)
                .build();
    }

    @Transactional
    public ResponseBlockUser blockUser(RequestBlockUser requestBlockUser, Long userId) throws AppException {
        Long blockedUserId = requestBlockUser.getBlockedUserId();
        if (blockedUserId.equals(userId)) {
            throw new AppException(UserErrorCode.USER_CANNOT_BLOCK_SELF);
        }

        UserBlockEntity userBlockEntity = new UserBlockEntity();
        userBlockEntity.setStatus(Status.ACTIVE);
        userBlockEntity.setBlockerUserId(userId);
        userBlockEntity.setBlockedUserId(blockedUserId);
        userBlockJpaRepository.save(userBlockEntity);

        return ResponseBlockUser.builder()
                .blockedUserId(blockedUserId)
                .build();
    }

    @Transactional
    public ResponseUnblockUser unblockUser(RequestUnblockUser requestUnblockUser, Long userId) throws AppException {
        Long unblockedUserId = requestUnblockUser.getUnblockedUserId();
        if (unblockedUserId.equals(userId)) {
            throw new AppException(UserErrorCode.USER_CANNOT_UNBLOCK_SELF);
        }

        UserBlockEntity userBlockEntity = userBlockJpaRepository.findByBlockerUserIdAndBlockedUserId(userId, unblockedUserId)
                .orElseThrow(() -> new AppException(UserErrorCode.USER_BLOCK_NOT_FOUND));

        userBlockEntity.setStatus(Status.DELETED);
        userBlockJpaRepository.save(userBlockEntity);

        return ResponseUnblockUser.builder()
                .unblockedUserId(unblockedUserId)
                .build();
    }

    @Transactional
    public ResponseGetBlockedUsers getBlockedUsers(Long userId) {
        List<UserDto> blockerUserDtoList = userJpaRepository.findBlockedUsersByBlockerId(userId);

        return ResponseGetBlockedUsers.builder()
                .blockedUserDtoList(blockerUserDtoList)
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

    private void deleteRooms(Long userId) {
        List<RoomEntity> roomEntityList = roomJpaRepository.findByCreatorUserId(userId);
        roomEntityList.forEach(room -> {
            room.setStatus(Status.DELETED);
            room.getUsers().clear();
        });

        roomJpaRepository.saveAll(roomEntityList);
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
