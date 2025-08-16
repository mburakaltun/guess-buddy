package com.mburakaltun.guessbuddy.room.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.enums.Status;
import com.mburakaltun.guessbuddy.common.service.ContentFilterService;
import com.mburakaltun.guessbuddy.room.model.entity.RoomEntity;
import com.mburakaltun.guessbuddy.room.model.enums.RoomErrorCode;
import com.mburakaltun.guessbuddy.room.model.request.RequestCloseRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestCreateRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestJoinRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestLeaveRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseCloseRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseCreateRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseJoinRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseLeaveRoom;
import com.mburakaltun.guessbuddy.room.repository.RoomJpaRepository;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final ContentFilterService contentFilterService;
    private final RoomJpaRepository roomJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public ResponseCreateRoom createRoom(RequestCreateRoom requestCreateRoom, Long userId) throws AppException {
        Optional<UserEntity> userEntityOptional = userJpaRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new AppException(AuthenticationErrorCode.USER_NOT_FOUND);
        }

        contentFilterService.validateContent(requestCreateRoom.getTitle());

        String passcode = generateUniquePasscode();

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setTitle(requestCreateRoom.getTitle());
        roomEntity.setCreatorUserId(userEntityOptional.get().getId());
        roomEntity.setPasscode(passcode);
        roomJpaRepository.save(roomEntity);

        return ResponseCreateRoom.builder()
                .roomPasscode(passcode)
                .build();
    }

    public ResponseJoinRoom joinRoom(RequestJoinRoom requestJoinRoom, Long userId) throws AppException {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        String passcode = requestJoinRoom.getPasscode();
        RoomEntity roomEntity = roomJpaRepository.findByPasscode(passcode)
                .orElseThrow(() -> new AppException(RoomErrorCode.ROOM_NOT_FOUND));

        if (roomEntity.getCreatorUserId().equals(userId)) {
            throw new AppException(RoomErrorCode.CANNOT_JOIN_OWN_ROOM);
        }

        if (roomEntity.getUsers().contains(user)) {
            log.warn("User with ID {} is already in room with ID {}", userId, roomEntity.getId());
            return ResponseJoinRoom.builder()
                    .isAlreadyInRoom(true)
                    .roomId(roomEntity.getId())
                    .build();
        }

        return ResponseJoinRoom.builder()
                .isAlreadyInRoom(false)
                .roomId(roomEntity.getId())
                .build();
    }

    public ResponseLeaveRoom leaveRoom(RequestLeaveRoom requestLeaveRoom, Long userId) throws AppException {
        Long roomId = requestLeaveRoom.getRoomId();
        RoomEntity roomEntity = roomJpaRepository.findById(roomId)
                .orElseThrow(() -> new AppException(RoomErrorCode.ROOM_NOT_FOUND));

        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        if (roomEntity.getCreatorUserId().equals(userId)) {
            log.warn("User with ID {} is the creator of room with ID {} and cannot leave", userId, roomId);
            return ResponseLeaveRoom.builder()
                    .isAlreadyLeft(true)
                    .build();
        }

        if (!roomEntity.getUsers().contains(user)) {
            log.warn("User with ID {} is not in room with ID {}", userId, roomId);
            return ResponseLeaveRoom.builder()
                    .isAlreadyLeft(true)
                    .build();
        }

        roomEntity.getUsers().remove(user);
        roomJpaRepository.save(roomEntity);

        return ResponseLeaveRoom.builder()
                .isAlreadyLeft(false)
                .build();
    }

    public ResponseCloseRoom closeRoom(RequestCloseRoom requestCloseRoom, Long userId) throws AppException {
        Long roomId = requestCloseRoom.getRoomId();
        RoomEntity roomEntity = roomJpaRepository.findById(roomId)
                .orElseThrow(() -> new AppException(RoomErrorCode.ROOM_NOT_FOUND));

        if (!roomEntity.getCreatorUserId().equals(userId)) {
            throw new AppException(RoomErrorCode.NOT_ROOM_CREATOR);
        }

        roomEntity.setStatus(Status.DELETED);
        roomJpaRepository.save(roomEntity);

        return ResponseCloseRoom.builder()
                .build();
    }


    private String generateUniquePasscode() {
        String passcode;
        do {
            passcode = generateSixDigitPasscode();
        } while (roomJpaRepository.existsByPasscode(passcode));
        return passcode;
    }

    private String generateSixDigitPasscode() {
        int passcode = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(passcode);
    }
}
