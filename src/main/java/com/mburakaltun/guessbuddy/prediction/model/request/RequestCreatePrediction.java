package com.mburakaltun.guessbuddy.prediction.model.request;

import com.mburakaltun.guessbuddy.common.annotation.CleanContent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreatePrediction {
    @NotBlank(message = "{validation.title.notBlank}")
    @Size(min = 5, max = 255, message = "{validation.title.size}")
    @CleanContent(message = "{validation.title.filtered}")
    private String title;

    @NotBlank(message = "{validation.description.notBlank}")
    @Size(min = 5, max = 255, message = "{validation.description.size}")
    @CleanContent(message = "{validation.description.filtered}")
    private String description;
}
