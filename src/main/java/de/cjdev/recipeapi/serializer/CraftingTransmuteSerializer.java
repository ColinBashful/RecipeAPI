package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.CustomIngredient;
import de.cjdev.recipeapi.api.recipe.CustomRecipe;
import de.cjdev.recipeapi.api.recipe.CustomTransmuteRecipe;

public class CraftingTransmuteSerializer extends CraftingRecipeSerializer {
    public CustomIngredient input;
    public CustomIngredient material;

    @Override
    public CustomRecipe<?> asRecipe() {
        return new CustomTransmuteRecipe(getResult(), input, material);
    }
}
