package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.CustomBlastingRecipe;
import de.cjdev.recipeapi.api.recipe.CustomRecipe;

public class BlastingSerializer extends CookingRecipeSerializer {
    @Override
    public CustomRecipe<?> asRecipe() {
        CustomBlastingRecipe recipe = new CustomBlastingRecipe(getResult(), ingredient, experience, cookingtime);
        apply(recipe);
        return recipe;
    }
}
