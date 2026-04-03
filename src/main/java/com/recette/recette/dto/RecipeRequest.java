package com.recette.recette.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RecipeRequest(
        @NotBlank String title,
        @Size(max = 2000) String description,
        @Size(max = 500) String imageUrl,
        @Valid @NotEmpty List<IngredientRequest> ingredients,
        @Valid @NotEmpty List<PreparationStepRequest> preparationSteps
) {
}
