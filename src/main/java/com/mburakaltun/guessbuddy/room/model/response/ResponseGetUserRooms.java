package com.mburakaltun.guessbuddy.room.model.response;

import com.mburakaltun.guessbuddy.room.model.dto.UserRoomDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseGetUserRooms {
    private List<UserRoomDto> userRooms;
}
