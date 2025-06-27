package de.cjdev.recipeapi.api.recipe;

import de.cjdev.recipeapi.api.Helper;
import de.cjdev.recipeapi.RecipeAPI;
import de.cjdev.recipeapi.api.item.DyeItem;
import de.cjdev.recipeapi.api.item.GenericItemInfo;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomDyeItemRecipe implements CustomCraftingRecipe {

    public CustomDyeItemRecipe() {
    }

    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CustomCraftingInput craftingInput) {
        long[] dyeFlags = DyeItem.getDyeFlags(craftingInput);
        long[] inputFlag = craftingInput.getInputFlag();
        int itemToDye = -1;
        FIND_LOOP:
        {
            for (int i = 0; i < inputFlag.length; ++i) {
                long difference = inputFlag[i] ^ dyeFlags[i];
                if (difference == 0)
                    continue;
                itemToDye = (i << 6) + Long.numberOfTrailingZeros(difference);
                break FIND_LOOP;
            }
        }
        if (itemToDye == -1)
            return ItemStack.empty();

        List<Color> colors = DyeItem.getDyeColors(craftingInput);
        return apply(craftingInput.getItem(itemToDye), colors);
    }

    private static ItemStack apply(@NotNull ItemStack stack, List<Color> colors) {
        GenericItemInfo itemInfo = RecipeAPI.getItemInfo(stack);
        if (!itemInfo.dyeable())
            return ItemStack.empty();

        ItemStack itemStack = stack.asOne();
        Helper.applyColors(itemStack, colors);
        return itemStack;
    }

    @Override
    public boolean matches(@NotNull CustomCraftingInput input) {
        if (input.ingredientCount() < 2 || input.ingredientCount() != DyeItem.getDyeCount(input) + 1)
            return false;

        long[] dyeFlags = DyeItem.getDyeFlags(input);
        long[] inputFlag = input.getInputFlag();
        for (int i = 0; i < inputFlag.length; ++i) {
            long difference = inputFlag[i] ^ dyeFlags[i];
            if (difference == 0)
                continue;
            ItemStack dyeThis = input.getItem(i * 64 + Long.numberOfTrailingZeros(difference));
            GenericItemInfo itemInfo = RecipeAPI.getItemInfo(dyeThis);
            if (!itemInfo.air() && !itemInfo.dyeable())
                return false;
        }

        return true;
    }

    @Override
    public Recipe toBukketRecipe(@NotNull NamespacedKey key) {
        return null;
    }
}
