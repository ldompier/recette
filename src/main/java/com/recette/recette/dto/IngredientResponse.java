package com.recette.recette.dto;

public record IngredientResponse(
        Long id,
        String name,
        String quantity,
        Integer displayOrder
) {
}
