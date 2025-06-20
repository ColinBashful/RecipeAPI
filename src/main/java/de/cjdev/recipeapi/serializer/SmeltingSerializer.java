package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.CustomRecipe;
import de.cjdev.recipeapi.api.recipe.CustomSmeltingRecipe;

public class SmeltingSerializer extends CookingRecipeSerializer {
    @Override
    public CustomRecipe<?> asRecipe() {
        CustomSmeltingRecipe recipe = new CustomSmeltingRecipe(getResult(), ingredient, experience, cookingtime);
        apply(recipe);
        return recipe;
    }
}
