package com.ivbaranovskii.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ivbaranovskii.drones.enums.DroneModel;
import com.ivbaranovskii.drones.enums.DroneState;
import jakarta.validation.constraints.*;

public record DroneDto(
        @JsonProperty(required = false)
        Long id,

        @JsonProperty(required = true)
        @NotBlank(message = "serial number must not be null, blank or empty")
        @Size(max = 100, message = "serial number length must not exceed 100 characters")
        String serialNumber,

        @JsonProperty(required = true)
        @NotNull(message = "drone model must not be null")
        DroneModel model,

        @JsonProperty(required = true)
        @NotNull(message = "drone weightLimit value must not be null")
        @Min(value = 0, message = "drone weightLimit must be greater or equal to 0")
        @Max(value = 500, message = "drone weightLimit must not exceed 500")
        Integer weightLimit,

        @JsonProperty(required = true)
        @NotNull(message = "drone battery level value must not be null")
        @Min(value = 0, message = "drone battery level must be greater or equal to 0")
        @Max(value = 100, message = "drone battery level must not exceed 100")
        Float battery,

        @JsonProperty(required = true)
        @NotNull(message = "drone state must not be null")
        DroneState state
) {
}
