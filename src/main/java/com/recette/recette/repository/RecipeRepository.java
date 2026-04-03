package com.recette.recette.repository;

import com.recette.recette.domain.Ingredient;
import com.recette.recette.domain.PreparationStep;
import com.recette.recette.domain.Recipe;
import com.recette.recette.dto.IngredientRequest;
import com.recette.recette.dto.PreparationStepRequest;
import com.recette.recette.dto.RecipeRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RecipeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RecipeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Recipe> findAll() {
        String sql = """
                SELECT id, title, description, image_url
                FROM recipe
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("image_url"),
                List.of(),
                List.of()
        ));
    }

    public Optional<Recipe> findById(Long id) {
        String sql = """
                SELECT id, title, description, image_url
                FROM recipe
                WHERE id = :id
                """;

        List<Recipe> recipes = jdbcTemplate.query(sql, Map.of("id", id), (rs, rowNum) -> new Recipe(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("image_url"),
                List.of(),
                List.of()
        ));

        return recipes.stream().findFirst().map(recipe -> new Recipe(
                recipe.id(),
                recipe.title(),
                recipe.description(),
                recipe.imageUrl(),
                findIngredientsByRecipeId(recipe.id()),
                findPreparationStepsByRecipeId(recipe.id())
        ));
    }

    @Transactional
    public Recipe create(RecipeRequest request) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource recipeParams = new MapSqlParameterSource()
                .addValue("title", request.title())
                .addValue("description", request.description())
                .addValue("imageUrl", request.imageUrl());

        jdbcTemplate.update("""
                        INSERT INTO recipe (title, description, image_url)
                        VALUES (:title, :description, :imageUrl)
                        """,
                recipeParams,
                keyHolder,
                new String[]{"id"});

        Long recipeId = extractGeneratedId(keyHolder);
        insertIngredients(recipeId, request.ingredients());
        insertPreparationSteps(recipeId, request.preparationSteps());

        return findById(recipeId).orElseThrow();
    }

    @Transactional
    public Recipe update(Long id, RecipeRequest request) {
        jdbcTemplate.update("""
                        UPDATE recipe
                        SET title = :title,
                            description = :description,
                            image_url = :imageUrl
                        WHERE id = :id
                        """,
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("title", request.title())
                        .addValue("description", request.description())
                        .addValue("imageUrl", request.imageUrl()));

        jdbcTemplate.update("DELETE FROM ingredient WHERE recipe_id = :id", Map.of("id", id));
        jdbcTemplate.update("DELETE FROM preparation_step WHERE recipe_id = :id", Map.of("id", id));

        insertIngredients(id, request.ingredients());
        insertPreparationSteps(id, request.preparationSteps());

        return findById(id).orElseThrow();
    }

    @Transactional
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM ingredient WHERE recipe_id = :id", Map.of("id", id));
        jdbcTemplate.update("DELETE FROM preparation_step WHERE recipe_id = :id", Map.of("id", id));
        jdbcTemplate.update("DELETE FROM recipe WHERE id = :id", Map.of("id", id));
    }

    private void insertIngredients(Long recipeId, List<IngredientRequest> ingredients) {
        for (IngredientRequest ingredient : ingredients) {
            jdbcTemplate.update("""
                            INSERT INTO ingredient (recipe_id, name, quantity, display_order)
                            VALUES (:recipeId, :name, :quantity, :displayOrder)
                            """,
                    new MapSqlParameterSource()
                            .addValue("recipeId", recipeId)
                            .addValue("name", ingredient.name())
                            .addValue("quantity", ingredient.quantity())
                            .addValue("displayOrder", ingredient.displayOrder()));
        }
    }

    private void insertPreparationSteps(Long recipeId, List<PreparationStepRequest> steps) {
        for (PreparationStepRequest step : steps) {
            jdbcTemplate.update("""
                            INSERT INTO preparation_step (recipe_id, step_number, description)
                            VALUES (:recipeId, :stepNumber, :description)
                            """,
                    new MapSqlParameterSource()
                            .addValue("recipeId", recipeId)
                            .addValue("stepNumber", step.stepNumber())
                            .addValue("description", step.description()));
        }
    }

    private List<Ingredient> findIngredientsByRecipeId(Long recipeId) {
        return jdbcTemplate.query("""
                        SELECT id, name, quantity, display_order
                        FROM ingredient
                        WHERE recipe_id = :recipeId
                        ORDER BY display_order, id
                        """,
                Map.of("recipeId", recipeId),
                (rs, rowNum) -> new Ingredient(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("quantity"),
                        rs.getInt("display_order")
                ));
    }

    private List<PreparationStep> findPreparationStepsByRecipeId(Long recipeId) {
        return jdbcTemplate.query("""
                        SELECT id, step_number, description
                        FROM preparation_step
                        WHERE recipe_id = :recipeId
                        ORDER BY step_number, id
                        """,
                Map.of("recipeId", recipeId),
                (rs, rowNum) -> new PreparationStep(
                        rs.getLong("id"),
                        rs.getInt("step_number"),
                        rs.getString("description")
                ));
    }

    private Long extractGeneratedId(GeneratedKeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key != null) {
            return key.longValue();
        }

        Object value = keyHolder.getKeys() != null ? keyHolder.getKeys().get("id") : null;
        if (value instanceof Number number) {
            return number.longValue();
        }

        throw new IllegalStateException("Impossible de recuperer l'identifiant genere");
    }
}
