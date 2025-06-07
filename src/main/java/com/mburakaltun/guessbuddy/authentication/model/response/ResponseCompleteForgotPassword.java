package com.mburakaltun.guessbuddy.authentication.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCompleteForgotPassword {
    private String userId;
}
