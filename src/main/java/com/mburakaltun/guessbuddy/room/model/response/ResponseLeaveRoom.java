package com.mburakaltun.guessbuddy.room.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLeaveRoom {
    private Boolean isAlreadyLeft;
    private Long roomId;
}
