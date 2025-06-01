package com.mburakaltun.guessbuddy.authentication.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpUserRequest {

    @NotBlank(message = "Email must not be null or empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be null or empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Password confirmation must not be null or empty")
    private String confirmPassword;
}
