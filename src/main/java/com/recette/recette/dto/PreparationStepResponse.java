package com.recette.recette.dto;

public record PreparationStepResponse(
        Long id,
        Integer stepNumber,
        String description
) {
}
