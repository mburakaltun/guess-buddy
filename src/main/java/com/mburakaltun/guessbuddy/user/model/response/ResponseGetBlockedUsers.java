package com.mburakaltun.guessbuddy.user.model.response;

import com.mburakaltun.guessbuddy.user.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGetBlockedUsers {
    private List<UserDto> blockerUserDtoList;
}
