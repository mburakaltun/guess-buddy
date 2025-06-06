package com.mburakaltun.guessbuddy.prediction.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreatePrediction {
    @NotBlank(message = "{validation.title.notBlank}")
    @Size(min = 5, max = 255, message = "{validation.title.size}")
    private String title;

    @NotBlank(message = "{validation.description.notBlank}")
    @Size(min = 5, max = 255, message = "{validation.description.size}")
    private String description;
}
