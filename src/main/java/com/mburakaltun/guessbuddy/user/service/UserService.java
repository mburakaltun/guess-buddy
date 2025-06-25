package com.mburakaltun.guessbuddy.user.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.enums.Status;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

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

    public ResponseChangePassword changePassword(RequestChangePassword requestChangePassword, String userId) throws AppException {
        if (!requestChangePassword.getNewPassword().equals(requestChangePassword.getConfirmNewPassword())) {
            throw new AppException(UserErrorCode.PASSWORD_MISMATCH);
        }

        UserEntity userEntity = userJpaRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));
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
    public ResponseDeleteUser deleteUser(String userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        userEntity.setStatus(Status.DELETED);
        userJpaRepository.save(userEntity);

        return ResponseDeleteUser.builder()
                .userId(Long.valueOf(userId))
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
