package com.mburakaltun.guessbuddy.room.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoomDto {
    private Boolean isHost;
    private Long roomId;
    private String passcode;
    private String roomTitle;
}
