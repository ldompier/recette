package com.recette.recette.domain;

import java.util.List;

public record Recipe(
        Long id,
        String title,
        String description,
        String imageUrl,
        List<Ingredient> ingredients,
        List<PreparationStep> preparationSteps
) {
}
