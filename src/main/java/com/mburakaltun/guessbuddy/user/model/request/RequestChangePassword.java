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
public class RequestChangePassword {

    @NotBlank(message = "{validation.password.notBlank}")
    private String currentPassword;

    @NotBlank(message = "{validation.password.notBlank}")
    @Size(min = 8, max = 50, message = "{validation.password.size}")
    private String newPassword;

    @NotBlank(message = "{validation.confirmPassword.notBlank}")
    private String confirmNewPassword;
}