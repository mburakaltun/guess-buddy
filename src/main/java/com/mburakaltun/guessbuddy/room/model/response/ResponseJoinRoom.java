package com.mburakaltun.guessbuddy.room.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseJoinRoom {
    private Boolean isAlreadyInRoom;
    private Long roomId;
}
