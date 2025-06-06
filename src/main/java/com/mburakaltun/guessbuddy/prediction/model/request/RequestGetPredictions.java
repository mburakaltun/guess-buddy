package com.mburakaltun.guessbuddy.prediction.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestGetPredictions {
    @NotNull(message = "{validation.page.notNull}")
    @Min(value = 0, message = "{validation.page.min}")
    private Integer page;

    @NotNull(message = "{validation.size.notNull}")
    @Min(value = 1, message = "{validation.size.min}")
    private Integer size;
}
