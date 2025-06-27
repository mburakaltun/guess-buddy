package com.mburakaltun.guessbuddy.authentication.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRefreshToken {
    
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}