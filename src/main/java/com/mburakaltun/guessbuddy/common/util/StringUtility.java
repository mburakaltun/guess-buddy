package com.mburakaltun.guessbuddy.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtility {
    public final String EMPTY = "";
    public final String SPACE = " ";
    public final String NEWLINE = "\n";
    public final String TAB = "\t";
    public final String BEARER_PREFIX = "Bearer ";
    public final String COMMA = ",";

    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
