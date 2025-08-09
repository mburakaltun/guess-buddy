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
public class RequestUnblockUser {
    @NotNull(message = "{validation.user.unblockedUserId.notNull}")
    private Long unblockedUserId;
}
