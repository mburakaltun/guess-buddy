package com.mburakaltun.guessbuddy.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    DELETED(-1),
    PASSIVE(0),
    ACTIVE(1);

    private final int value;
}