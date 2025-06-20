package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.CustomRecipe;
import de.cjdev.recipeapi.api.recipe.CustomSmokingRecipe;

public class SmokingSerializer extends CookingRecipeSerializer {
    @Override
    public CustomRecipe<?> asRecipe() {
        CustomSmokingRecipe recipe = new CustomSmokingRecipe(getResult(), ingredient, experience, cookingtime);
        apply(recipe);
        return recipe;
    }
}
