package com.recette.recette.dto;

public record RecipeSummaryResponse(
        Long id,
        String title,
        String description,
        String imageUrl
) {
}
