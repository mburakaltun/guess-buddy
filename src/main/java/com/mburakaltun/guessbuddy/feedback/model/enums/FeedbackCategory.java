package com.mburakaltun.guessbuddy.feedback.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedbackCategory {
    SUGGESTION(1),
    BUG_REPORT(2),
    QUESTION(3),
    OTHER(4);

    private final int code;
}
