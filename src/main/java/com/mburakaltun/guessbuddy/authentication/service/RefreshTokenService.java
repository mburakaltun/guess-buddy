package com.mburakaltun.guessbuddy.authentication.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.RefreshTokenEntity;
import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.authentication.repository.RefreshTokenJpaRepository;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.service.JwtService;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final JwtService jwtService;

    @Transactional
    public RefreshTokenEntity createRefreshToken(Long userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        String token = jwtService.generateRefreshToken();
        LocalDateTime expiryDate = jwtService.calculateRefreshTokenExpiryDate();

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(token);
        refreshTokenEntity.setUser(userEntity);
        refreshTokenEntity.setExpiryDate(expiryDate);

        return refreshTokenJpaRepository.save(refreshTokenEntity);
    }

    @Transactional
    public RefreshTokenEntity validateRefreshToken(String token) throws AppException {
        RefreshTokenEntity refreshTokenEntity = refreshTokenJpaRepository.findByToken(token)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshTokenEntity.isExpired()) {
            refreshTokenJpaRepository.delete(refreshTokenEntity);
            throw new AppException(AuthenticationErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        return refreshTokenEntity;
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenJpaRepository.deleteByToken(token);
    }

    @Transactional
    public void deleteAllUserRefreshTokens(Long userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        refreshTokenJpaRepository.deleteAllByUser(userEntity);
    }
}