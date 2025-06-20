package de.cjdev.recipeapi.api.recipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface CustomSmithingRecipe extends CustomRecipe<CustomSmithingRecipeInput> {
    default boolean matches(@NotNull CustomSmithingRecipeInput craftingInput) {
        return CustomIngredient.testOptionalIngredient(this.templateIngredient(), craftingInput.template()) && CustomIngredient.testOptionalIngredient(this.baseIngredient(), craftingInput.base()) && CustomIngredient.testOptionalIngredient(this.additionIngredient(), craftingInput.addition());
    }

    @Nullable CustomIngredient templateIngredient();

    @Nullable CustomIngredient baseIngredient();

    @Nullable CustomIngredient additionIngredient();
}
