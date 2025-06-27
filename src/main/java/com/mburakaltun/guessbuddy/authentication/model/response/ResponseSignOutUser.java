package com.mburakaltun.guessbuddy.authentication.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSignOutUser {
    private boolean success;
    private String message;
}