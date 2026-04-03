package com.recette.recette.domain;

public record PreparationStep(
        Long id,
        Integer stepNumber,
        String description
) {
}
