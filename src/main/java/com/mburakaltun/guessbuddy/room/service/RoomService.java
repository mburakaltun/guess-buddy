package com.mburakaltun.guessbuddy.room.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.enums.Status;
import com.mburakaltun.guessbuddy.prediction.model.entity.PredictionEntity;
import com.mburakaltun.guessbuddy.prediction.repository.PredictionJpaRepository;
import com.mburakaltun.guessbuddy.room.model.dto.UserRoomDto;
import com.mburakaltun.guessbuddy.room.model.entity.RoomEntity;
import com.mburakaltun.guessbuddy.room.model.enums.RoomErrorCode;
import com.mburakaltun.guessbuddy.room.model.request.RequestCloseRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestCreateRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestGetUserRooms;
import com.mburakaltun.guessbuddy.room.model.request.RequestJoinRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestLeaveRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseCloseRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseCreateRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseGetUserRooms;
import com.mburakaltun.guessbuddy.room.model.response.ResponseJoinRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseLeaveRoom;
import com.mburakaltun.guessbuddy.room.model.utility.RoomMapper;
import com.mburakaltun.guessbuddy.room.repository.RoomJpaRepository;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import com.mburakaltun.guessbuddy.vote.model.entity.VoteEntity;
import com.mburakaltun.guessbuddy.vote.repository.VoteJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final RoomJpaRepository roomJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PredictionJpaRepository predictionJpaRepository;
    private final VoteJpaRepository voteJpaRepository;

    @Transactional
    public ResponseCreateRoom createRoom(RequestCreateRoom requestCreateRoom, Long userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        String passcode = generateUniquePasscode();

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setTitle(requestCreateRoom.getTitle());
        roomEntity.setCreatorUserId(userEntity.getId());
        roomEntity.setPasscode(passcode);
        roomEntity.setUsers(Set.of(userEntity));
        roomJpaRepository.save(roomEntity);

        return ResponseCreateRoom.builder()
                .roomId(roomEntity.getId())
                .roomTitle(roomEntity.getTitle())
                .passcode(passcode)
                .build();
    }

    public ResponseJoinRoom joinRoom(RequestJoinRoom requestJoinRoom, Long userId) throws AppException {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        String passcode = requestJoinRoom.getPasscode();
        RoomEntity roomEntity = roomJpaRepository.findByPasscode(passcode)
                .orElseThrow(() -> new AppException(RoomErrorCode.ROOM_NOT_FOUND));

        if (roomEntity.getUsers().contains(user)) {
            log.warn("User with ID {} is already in room with ID {}", userId, roomEntity.getId());
            return ResponseJoinRoom.builder()
                    .isAlreadyInRoom(true)
                    .roomId(roomEntity.getId())
                    .roomTitle(roomEntity.getTitle())
                    .build();
        }

        roomEntity.getUsers().add(user);
        roomJpaRepository.save(roomEntity);

        return ResponseJoinRoom.builder()
                .isAlreadyInRoom(false)
                .roomId(roomEntity.getId())
                .roomTitle(roomEntity.getTitle())
                .build();
    }

    @Transactional
    public ResponseLeaveRoom leaveRoom(RequestLeaveRoom requestLeaveRoom, Long userId, Long roomId) throws AppException {
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

        List<PredictionEntity> predictionEntityList = predictionJpaRepository.findByCreatorUserIdAndRoomId(userId, roomId);
        predictionEntityList
                .forEach(predictionEntity -> {
                    predictionEntity.setStatus(Status.DELETED);
                    predictionJpaRepository.save(predictionEntity);
                });

        List<Long> predictionIds = predictionEntityList.stream()
                .map(PredictionEntity::getId)
                .toList();

        List<VoteEntity> voteEntityList = voteJpaRepository.findByPredictionIdIn(predictionIds);
        voteEntityList
                .forEach(voteEntity -> {
                    voteEntity.setStatus(Status.DELETED);
                    voteJpaRepository.save(voteEntity);
                });

        List<VoteEntity> userVotes = voteJpaRepository.findByVoterUserIdAndRoomId(userId, roomId);
        userVotes
                .forEach(voteEntity -> {
                    voteEntity.setStatus(Status.DELETED);
                    voteJpaRepository.save(voteEntity);
                });

        roomEntity.getUsers().remove(user);
        roomJpaRepository.save(roomEntity);

        return ResponseLeaveRoom.builder()
                .isAlreadyLeft(false)
                .build();
    }

    public ResponseCloseRoom closeRoom(RequestCloseRoom requestCloseRoom, Long userId, Long roomId) throws AppException {
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

    public ResponseGetUserRooms getUserRooms(RequestGetUserRooms requestGetUserRooms, Long userId) throws AppException {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.USER_NOT_FOUND));

        // todo: Implement pagination if needed
        int page = requestGetUserRooms.getPage();
        int size = requestGetUserRooms.getSize();

        List<UserRoomDto> userRoomDtoList = userEntity.getRooms()
                .stream()
                .map(roomEntity -> RoomMapper.toDto(roomEntity, userId))
                .toList();

        return ResponseGetUserRooms.builder()
                .userRooms(userRoomDtoList)
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
