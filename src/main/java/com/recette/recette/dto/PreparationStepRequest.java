package com.recette.recette.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PreparationStepRequest(
        @NotNull @Min(1) Integer stepNumber,
        @NotBlank String description
) {
}
