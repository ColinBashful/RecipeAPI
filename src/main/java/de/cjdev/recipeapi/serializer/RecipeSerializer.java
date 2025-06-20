package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.RecipeAPI;
import de.cjdev.recipeapi.api.recipe.CustomRecipe;
import org.bukkit.inventory.ItemStack;

public abstract class RecipeSerializer {
    public String group;
    public RecipeAPI.ResultStack result;

    public ItemStack getResult() {
        return result.handle();
    }

    public abstract CustomRecipe<?> asRecipe();
}
