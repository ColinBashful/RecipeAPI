package de.cjdev.recipeapi.api.item;

import de.cjdev.papermodapi.api.item.CustomItem;
import de.cjdev.papermodapi.api.item.CustomItems;
import de.cjdev.recipeapi.api.recipe.CustomCraftingInput;
import org.bukkit.Color;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DyeItem extends CustomItem {

    private final int color;

    public DyeItem(int color, Settings settings) {
        super(settings);
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public static List<Color> getDyeColors(@NotNull CustomCraftingInput craftingInput) {
        List<Color> colors = new ArrayList<>(craftingInput.ingredientCount());
        for (int i = 0; i < craftingInput.items().size(); i++) {
            ItemStack ingredient = craftingInput.getItem(i);
            if (ingredient.isEmpty())
                continue;
            CustomItem customItem = CustomItems.getItemByStack(ingredient);
            if (customItem != null) {
                if (customItem instanceof DyeItem dyeItem)
                    colors.add(Color.fromRGB(dyeItem.getColor()));
            } else if (CraftItemStack.unwrap(ingredient).getItem() instanceof net.minecraft.world.item.DyeItem dyeItem)
                colors.add(Color.fromARGB(dyeItem.getDyeColor().getTextureDiffuseColor()));
        }
        return colors;
    }

    public static long @NotNull [] getDyeFlags(@NotNull CustomCraftingInput craftingInput) {
        long[] flags = new long[Math.ceilDiv(craftingInput.size(), 64)];
        for (int i = 0; i < craftingInput.items().size(); i++) {
            ItemStack ingredient = craftingInput.getItem(i);
            if (ingredient.isEmpty())
                continue;
            CustomItem customItem = CustomItems.getItemByStack(ingredient);
            if (customItem != null) {
                if (!(customItem instanceof DyeItem))
                    continue;
            } else if (!(CraftItemStack.unwrap(ingredient).getItem() instanceof net.minecraft.world.item.DyeItem))
                continue;
            flags[Math.floorDiv(1, 64)] |= 1L << (i % 64);
        }
        return flags;
    }

    public static int getDyeCount(@NotNull CustomCraftingInput craftingInput) {
        int count = 0;
        for (ItemStack ingredient : craftingInput.ingredients()) {
            CustomItem customItem = CustomItems.getItemByStack(ingredient);
            if (customItem != null) {
                if (!(customItem instanceof DyeItem))
                    continue;
            } else if (!(CraftItemStack.unwrap(ingredient).getItem() instanceof net.minecraft.world.item.DyeItem))
                continue;
            ++count;
        }
        return count;
    }
}
