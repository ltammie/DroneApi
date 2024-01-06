package com.ivbaranovskii.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record MedicineDto(
        Long id,

        @JsonProperty(required = true)
        @NotBlank(message = "medicine name must not be null")
        @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "medicine name can only contain letters, numbers, dash and underscore")
        String name,

        @JsonProperty(required = true)
        @NotNull(message = "medicine weight value must not be null")
        @Min(value = 0, message = "medicine weight must be greater or equal to 0")
        Double weight,

        @JsonProperty(required = true)
        @NotBlank(message = "medicine code must not be null")
        @Pattern(regexp = "^[A-Z0-9_]+$", message = "medicine code can only contain upperCase letters, numbers and underscore")
        String code,

        @JsonProperty(required = true)
        @NotEmpty(message = "medicine image must not be null or empty")
        @Pattern(regexp = "^[A-Za-z0-9+/]+={0,2}$", message = "image must be a correct base64 string without MIME type")
        String image
) {
}
