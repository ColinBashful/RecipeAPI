package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.AbstractCookingRecipe;
import de.cjdev.recipeapi.api.recipe.CustomIngredient;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.jetbrains.annotations.Nullable;

public abstract class CookingRecipeSerializer extends RecipeSerializer {
    public String category;
    public CustomIngredient ingredient;
    public float experience;
    public int cookingtime;

    public @Nullable CookingBookCategory getCategory() {
        if (category == null) return null;
        try {
            return CookingBookCategory.valueOf(category);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public void apply(AbstractCookingRecipe recipe) {
        CookingBookCategory category1 = getCategory();
        if (category1 != null)
            recipe.setCategory(category1);
        if (group != null)
            recipe.setGroup(group);
    }
}
