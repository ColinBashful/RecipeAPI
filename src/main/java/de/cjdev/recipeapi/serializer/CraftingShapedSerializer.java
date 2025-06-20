package de.cjdev.recipeapi.serializer;

import de.cjdev.recipeapi.api.recipe.CustomIngredient;
import de.cjdev.recipeapi.api.recipe.CustomRecipe;
import de.cjdev.recipeapi.api.recipe.CustomShapedRecipe;
import de.cjdev.recipeapi.api.recipe.CustomShapelessRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.Map;

public class CraftingShapedSerializer extends CraftingRecipeSerializer {
    public String[] pattern;
    public Map<String, CustomIngredient> key;

    @Override
    public CustomRecipe<?> asRecipe() {
        CustomShapedRecipe recipe = new CustomShapedRecipe(getResult());
        recipe.shape(pattern);
        key.forEach((s, customIngredient) -> recipe.setIngredient(s.charAt(0), customIngredient));
        CraftingBookCategory category1 = getCategory();
        if (category1 != null)
            recipe.setCategory(category1);
        if (group != null)
            recipe.setGroup(group);
        return recipe;
    }
}
