package de.cjdev.recipeapi.api.recipe;

import de.cjdev.papermodapi.api.item.CustomItem;
import de.cjdev.papermodapi.api.item.CustomItems;
import de.cjdev.recipeapi.api.item.DyeItem;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import org.bukkit.*;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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
                itemToDye = i * 64 + Long.numberOfTrailingZeros(difference);
                break FIND_LOOP;
            }
        }
        if (itemToDye == -1)
            return ItemStack.empty();

        List<Color> colors = DyeItem.getDyeColors(craftingInput);
        return apply(craftingInput.getItem(itemToDye), colors);
    }

    private static ItemStack apply(ItemStack stack, List<Color> colors) {
        CustomItem customItem = CustomItems.getItemByStack(stack);
        if (customItem != null) {
            if (!customItem.isDyeable())
                return ItemStack.empty();
        } else if (!Tag.ITEMS_DYEABLE.isTagged(stack.getType()))
            return ItemStack.empty();

        ItemStack itemStack = stack.asOne();
        int i = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        net.minecraft.world.item.component.DyedItemColor dyedItemColor = CraftItemStack.unwrap(itemStack).get(DataComponents.DYED_COLOR);
        if (dyedItemColor != null) {
            int i5 = ARGB.red(dyedItemColor.rgb());
            int i6 = ARGB.green(dyedItemColor.rgb());
            int i7 = ARGB.blue(dyedItemColor.rgb());
            i3 += Math.max(i5, Math.max(i6, i7));
            i += i5;
            i1 += i6;
            i2 += i7;
            ++i4;
        }

        for (Color color : colors) {
            int i7 = color.asARGB();
            int i8 = ARGB.red(i7);
            int i9 = ARGB.green(i7);
            int i10 = ARGB.blue(i7);
            i3 += Math.max(i8, Math.max(i9, i10));
            i += i8;
            i1 += i9;
            i2 += i10;
            ++i4;
        }

        int i5 = i / i4;
        int i6 = i1 / i4;
        int i7 = i2 / i4;
        float f = (float) i3 / (float) i4;
        float f1 = (float) Math.max(i5, Math.max(i6, i7));
        i5 = (int) ((float) i5 * f / f1);
        i6 = (int) ((float) i6 * f / f1);
        i7 = (int) ((float) i7 * f / f1);
        int i10 = ARGB.color(0, i5, i6, i7);
        boolean flag = dyedItemColor == null || dyedItemColor.showInTooltip();
        CraftItemStack.unwrap(itemStack).set(DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(i10, flag));
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
            CustomItem customItem = CustomItems.getItemByStack(dyeThis);
            if (customItem != null) {
                if (!customItem.isDyeable())
                    return false;
            } else if (!Tag.ITEMS_DYEABLE.isTagged(dyeThis.getType()))
                return false;
        }

        return true;
    }

    @Override
    public Recipe toBukketRecipe(@NotNull NamespacedKey key) {
        return null;
    }
}
