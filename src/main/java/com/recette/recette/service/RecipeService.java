package com.recette.recette.service;

import com.recette.recette.domain.Recipe;
import com.recette.recette.dto.IngredientResponse;
import com.recette.recette.dto.PreparationStepResponse;
import com.recette.recette.dto.RecipeRequest;
import com.recette.recette.dto.RecipeResponse;
import com.recette.recette.dto.RecipeSummaryResponse;
import com.recette.recette.exception.NotFoundException;
import com.recette.recette.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeSummaryResponse> findAll() {
        return recipeRepository.findAll().stream()
                .map(recipe -> new RecipeSummaryResponse(
                        recipe.id(),
                        recipe.title(),
                        recipe.description(),
                        recipe.imageUrl()
                ))
                .toList();
    }

    public RecipeResponse findById(Long id) {
        return recipeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Recette introuvable pour l'id " + id));
    }

    public RecipeResponse create(RecipeRequest request) {
        return toResponse(recipeRepository.create(request));
    }

    public RecipeResponse update(Long id, RecipeRequest request) {
        ensureExists(id);
        return toResponse(recipeRepository.update(id, request));
    }

    public void delete(Long id) {
        ensureExists(id);
        recipeRepository.delete(id);
    }

    private void ensureExists(Long id) {
        if (recipeRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Recette introuvable pour l'id " + id);
        }
    }

    private RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.id(),
                recipe.title(),
                recipe.description(),
                recipe.imageUrl(),
                recipe.ingredients().stream()
                        .map(ingredient -> new IngredientResponse(
                                ingredient.id(),
                                ingredient.name(),
                                ingredient.quantity(),
                                ingredient.displayOrder()
                        ))
                        .toList(),
                recipe.preparationSteps().stream()
                        .map(step -> new PreparationStepResponse(
                                step.id(),
                                step.stepNumber(),
                                step.description()
                        ))
                        .toList()
        );
    }
}
