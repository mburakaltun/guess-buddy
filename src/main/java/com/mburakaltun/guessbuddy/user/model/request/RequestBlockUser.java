package com.mburakaltun.guessbuddy.user.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestBlockUser {
    @NotNull(message = "{validation.user.blockedUserId.notNull}")
    private Long blockedUserId;
}
