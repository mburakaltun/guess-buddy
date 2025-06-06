package com.mburakaltun.guessbuddy.authentication.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSignInUser {
    private String authenticationToken;
    private String userId;
    private String username;
}
