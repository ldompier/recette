package com.recette.recette.dto;

import java.util.List;

public record RecipeResponse(
        Long id,
        String title,
        String description,
        String imageUrl,
        List<IngredientResponse> ingredients,
        List<PreparationStepResponse> preparationSteps
) {
}
