package com.mburakaltun.guessbuddy.common.model.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PageableResponse {
    private Integer number;
    private Long totalElements;
    private Integer totalPages;
    private Boolean isLast;
}
