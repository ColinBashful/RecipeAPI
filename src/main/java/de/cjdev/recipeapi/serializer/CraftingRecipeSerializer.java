package de.cjdev.recipeapi.serializer;

import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.Nullable;

public abstract class CraftingRecipeSerializer extends RecipeSerializer {
    public String category;

    public @Nullable CraftingBookCategory getCategory() {
        if (category == null) return null;
        try {
            return CraftingBookCategory.valueOf(category);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
