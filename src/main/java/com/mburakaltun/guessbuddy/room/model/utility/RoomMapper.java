package com.mburakaltun.guessbuddy.room.model.utility;

import com.mburakaltun.guessbuddy.room.model.dto.UserRoomDto;
import com.mburakaltun.guessbuddy.room.model.entity.RoomEntity;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class RoomMapper {
    public static UserRoomDto toDto(RoomEntity roomEntity, Long userId) {
        if (roomEntity == null) {
            return null;
        }
        return UserRoomDto.builder()
                .isHost(Objects.equals(roomEntity.getCreatorUserId(), userId))
                .roomId(roomEntity.getId())
                .passcode(roomEntity.getPasscode())
                .roomTitle(roomEntity.getTitle())
                .build();
    }
}
