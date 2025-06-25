package com.mburakaltun.guessbuddy.user.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.user.constants.UserCacheNames;
import com.mburakaltun.guessbuddy.user.model.enums.UserErrorCode;
import com.mburakaltun.guessbuddy.user.model.request.RequestChangeUsername;
import com.mburakaltun.guessbuddy.user.model.request.RequestGetUserProfile;
import com.mburakaltun.guessbuddy.user.model.response.ResponseChangeUsername;
import com.mburakaltun.guessbuddy.user.model.response.ResponseGetUserProfile;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

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
    public ResponseChangeUsername changeUsername(RequestChangeUsername request, String userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        String newUsername = request.getNewUsername();
        validateUsername(newUsername, userEntity.getId());

        userEntity.setUsername(newUsername);
        userJpaRepository.save(userEntity);

        return ResponseChangeUsername.builder()
                .id(userEntity.getId())
                .username(newUsername)
                .build();
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
