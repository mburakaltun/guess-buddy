package com.mburakaltun.guessbuddy.room.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreateRoom {
    @NotBlank(message = "{validation.roomTitle.notBlank}")
    private String title;
}
