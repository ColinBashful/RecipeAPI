package de.cjdev.recipeapi.api.recipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface CustomAnvilRecipe extends CustomRecipe<CustomAnvilRecipeInput> {
    default boolean matches(@NotNull CustomAnvilRecipeInput craftingInput) {
        return CustomIngredient.testOptionalIngredient(this.baseIngredient(), craftingInput.base()) && CustomIngredient.testOptionalIngredient(this.additionIngredient(), craftingInput.addition());
    }

    @Nullable CustomIngredient baseIngredient();

    @Nullable CustomIngredient additionIngredient();
}
