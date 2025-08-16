package com.mburakaltun.guessbuddy.room.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestLeaveRoom {
    @NotNull(message = "{validation.roomId.notNull}")
    private Long roomId;
}
