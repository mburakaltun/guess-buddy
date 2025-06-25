package com.mburakaltun.guessbuddy.user.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangeUsername {

    @NotBlank(message = "{validation.username.notBlank}")
    @Size(min = 3, max = 31, message = "{validation.username.size}")
    private String newUsername;
}