package com.mburakaltun.guessbuddy.authentication.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCompleteForgotPassword {
    @NotBlank(message = "{validation.token.notBlank}")
    private String token;

    @NotBlank(message = "{validation.password.notBlank}")
    @Size(min = 8, message = "{validation.password.size}")
    private String newPassword;
}
