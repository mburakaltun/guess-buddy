package com.mburakaltun.guessbuddy.room.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseJoinRoom {
    private Boolean isHost;
    private Long roomId;
    private String roomTitle;
    private String passcode;
}
