package de.cjdev.recipeapi.api.recipe;

import com.google.common.base.Predicates;
import de.cjdev.papermodapi.api.component.CustomDataComponents;
import de.cjdev.papermodapi.api.item.CustomItems;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.TransmuteRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomTransmuteRecipe implements CustomCraftingRecipe {
    private @NotNull String group = "";
    private CraftingBookCategory category = CraftingBookCategory.MISC;
    private final ItemStack result;
    private @NotNull final CustomIngredient input;
    private @NotNull final CustomIngredient material;

    public CustomTransmuteRecipe(@NotNull ItemStack result, @NotNull CustomIngredient input, @NotNull CustomIngredient material) {
        this.result = result;
        this.input = input;
        this.material = material;
    }

    public CustomTransmuteRecipe setGroup(@NotNull String group){
        this.group = group;
        return this;
    }

    public CustomTransmuteRecipe setCategory(@NotNull CraftingBookCategory category){
        this.category = category;
        return this;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return category;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CustomCraftingInput craftingInput) {
        ItemStack itemStack = ItemStack.empty();

        for (int i = 0; i < 2; ++i) {
            ItemStack item = craftingInput.ingredients().get(i);
            if (item.isEmpty() || !this.input.test(item) || Objects.equals(CustomItems.getKeyByStack(item, true), CustomItems.getKeyByStack(item, true)))
                continue;
            itemStack = item;
        }

        // TODO: Improve in 1.21.5 maybe
        ItemStack clone = itemStack.clone();
        CustomDataComponents.ITEM_COMPONENT.remove(clone);
        ItemStack resultStack = result.clone();
        resultStack.copyDataFrom(clone, Predicates.alwaysTrue());
        return resultStack;
    }

    @Override
    public boolean matches(@NotNull CustomCraftingInput craftingInput) {
        if (craftingInput.ingredientCount() != 2)
            return false;

        boolean resultDifferent = false;
        boolean foundMat = false;

        for (int i = 0; i < 2; ++i) {
            ItemStack item = craftingInput.ingredients().get(i);
            if (!item.isEmpty()) {
                if (!resultDifferent && this.input.test(item) && !Objects.equals(CustomItems.getKeyByStack(item, true), CustomItems.getKeyByStack(item, true))) {
                    resultDifferent = true;
                } else {
                    if (foundMat || !this.material.test(item))
                        return false;

                    foundMat = true;
                }
            }
        }

        return resultDifferent && foundMat;
    }

    @Override
    public @Nullable Recipe toBukketRecipe(@NotNull NamespacedKey key) {
        return new TransmuteRecipe(key, result.getType(), input.toBukkitRecipeChoice(), material.toBukkitRecipeChoice());
    }
}
