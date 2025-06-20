package de.cjdev.recipeapi.api.recipe;

import de.cjdev.papermodapi.api.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomCampfireCookingRecipe extends AbstractCookingRecipe {
    public CustomCampfireCookingRecipe(@NotNull ItemStack result, @NotNull CustomIngredient smeltIngredient, float experience, int cookingTime) {
        super(result, smeltIngredient, experience, cookingTime);
    }

    public CustomCampfireCookingRecipe(@NotNull Material result, @NotNull CustomIngredient smeltIngredient, float experience, int cookingTime) {
        this(ItemStack.of(result), smeltIngredient, experience, cookingTime);
    }

    public CustomCampfireCookingRecipe(@NotNull CustomItem result, @NotNull CustomIngredient smeltIngredient, float experience, int cookingTime) {
        this(result.getDefaultStack(), smeltIngredient, experience, cookingTime);
    }

    public CustomCampfireCookingRecipe(@NotNull ItemStack result, @NotNull CustomIngredient smeltIngredient, int cookingTime) {
        this(result, smeltIngredient, 0f, cookingTime);
    }

    public CustomCampfireCookingRecipe(@NotNull Material result, @NotNull CustomIngredient smeltIngredient, int cookingTime) {
        this(result, smeltIngredient, 0f, cookingTime);
    }

    public CustomCampfireCookingRecipe(@NotNull CustomItem result, @NotNull CustomIngredient smeltIngredient, int cookingTime) {
        this(result, smeltIngredient, 0f, cookingTime);
    }

    @Override
    public @Nullable CampfireRecipe toBukketRecipe(NamespacedKey key) {
        CampfireRecipe recipe = new CampfireRecipe(key, this.getOutput(), this.getIngredient().toBukkitRecipeChoice(), this.getExperience(), this.getCookingTime());
        recipe.setCategory(this.getCategory());
        recipe.setGroup(this.getGroup());
        return recipe;
    }
}
