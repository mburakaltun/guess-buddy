package com.mburakaltun.guessbuddy.common.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
    @NotNull(message = "{validation.page.notNull}")
    @Min(value = 0, message = "{validation.page.min}")
    private Integer page;

    @NotNull(message = "{validation.size.notNull}")
    @Min(value = 1, message = "{validation.size.min}")
    private Integer size;
}
