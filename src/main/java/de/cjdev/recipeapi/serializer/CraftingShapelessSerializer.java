package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.CustomIngredient;
import de.cjdev.recipeapi.api.recipe.CustomRecipe;
import de.cjdev.recipeapi.api.recipe.CustomShapelessRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public class CraftingShapelessSerializer extends CraftingRecipeSerializer {
    public CustomIngredient[] ingredients;

    @Override
    public CustomRecipe<?> asRecipe() {
        CustomShapelessRecipe recipe = new CustomShapelessRecipe(getResult());
        for (CustomIngredient ingredient : ingredients)
            recipe.addIngredient(ingredient);
        CraftingBookCategory category1 = getCategory();
        if (category1 != null)
            recipe.setCategory(category1);
        if (group != null)
            recipe.setGroup(group);
        return recipe;
    }
}
