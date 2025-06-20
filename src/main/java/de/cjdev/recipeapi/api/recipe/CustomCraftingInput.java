package de.cjdev.recipeapi.api.recipe;

import de.cjdev.papermodapi.api.item.CustomItems;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomCraftingInput implements CustomRecipeInput {
    public static final CustomCraftingInput EMPTY = new CustomCraftingInput(0, 0, List.of());
    private final int width;
    private final int height;
    private final List<ItemStack> items;
    private final List<ItemStack> ingredients;
    private final int ingredientCount;
    private final boolean anyCustom;
    private final long[] inputFlag;

    public CustomCraftingInput(int width, int height, @NotNull List<ItemStack> item) {
        this.width = width;
        this.height = height;
        this.items = Collections.unmodifiableList(item);
        int v = 0;
        boolean anyCustom = false;

        List<ItemStack> ingredients = new ArrayList<>();

        for (ItemStack itemStack : item) {
            if (!itemStack.isEmpty()) {
                ++v;
                ingredients.add(itemStack);
                if (CustomItems.isCustomStack(itemStack))
                    anyCustom = true;
            }
        }

        this.ingredientCount = v;
        this.ingredients = Collections.unmodifiableList(ingredients);
        this.anyCustom = anyCustom;

        long[] flags = new long[Math.ceilDiv(this.items.size(), 64)];
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack ingredient = this.items.get(i);
            if (ingredient.isEmpty())
                continue;
            flags[Math.floorDiv(1, 64)] |= 1L << (i % 64);
        }
        this.inputFlag = flags;
    }

    public boolean anyCustom(){
        return this.anyCustom;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public int ingredientCount() {
        return this.ingredientCount;
    }

    public boolean isEmpty() {
        return this.ingredientCount == 0;
    }

    public List<ItemStack> items() {
        return this.items;
    }

    public List<ItemStack> ingredients() {
        return this.ingredients;
    }

    ///
    /// @return Tells where Items are placed or not, via 1 and 0. Is a list for bigger Crafting Table Support
    /// E.g. ['110','010'] in bytes: \[1100100000000000000000000000000000000000000000000000000000000000]
    ///
    public long @NotNull [] getInputFlag() {
        return this.inputFlag;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public int size() {
        return this.items.size();
    }
}
