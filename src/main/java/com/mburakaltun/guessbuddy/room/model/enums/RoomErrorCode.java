package com.mburakaltun.guessbuddy.room.model.enums;

import com.mburakaltun.guessbuddy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoomErrorCode implements ErrorCode {
    ROOM_NOT_FOUND("ROOM_0001"),
    CANNOT_JOIN_OWN_ROOM("ROOM_0002"),
    NOT_ROOM_CREATOR("ROOM_0003");

    private final String code;
}
