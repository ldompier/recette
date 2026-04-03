package com.recette.recette.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientRequest(
        @NotBlank String name,
        @NotBlank String quantity,
        @NotNull @Min(1) Integer displayOrder
) {
}
