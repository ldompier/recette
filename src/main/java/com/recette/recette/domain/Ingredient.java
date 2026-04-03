package com.recette.recette.domain;

public record Ingredient(
        Long id,
        String name,
        String quantity,
        Integer displayOrder
) {
}
