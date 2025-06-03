package com.mburakaltun.guessbuddy.vote.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseVotePrediction {
    private boolean isVotedSuccessfully;
}
