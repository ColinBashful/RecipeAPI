package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.CustomCampfireCookingRecipe;
import de.cjdev.recipeapi.api.recipe.CustomRecipe;

public class CampfireCookingSerializer extends CookingRecipeSerializer {
    @Override
    public CustomRecipe<?> asRecipe() {
        CustomCampfireCookingRecipe recipe = new CustomCampfireCookingRecipe(getResult(), ingredient, experience, cookingtime);
        apply(recipe);
        return recipe;
    }
}
