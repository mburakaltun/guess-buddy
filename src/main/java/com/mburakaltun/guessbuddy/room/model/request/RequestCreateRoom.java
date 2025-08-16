package com.mburakaltun.guessbuddy.room.model.request;

import com.mburakaltun.guessbuddy.common.annotation.CleanContent;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreateRoom {
    @NotBlank(message = "{validation.roomTitle.notBlank}")
    @CleanContent(message = "{validation.roomTitle.filtered}")
    private String title;
}
