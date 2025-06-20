package de.cjdev.recipeapi.api.recipe;

import de.cjdev.papermodapi.api.item.CustomItems;
import org.bukkit.inventory.ItemStack;

public class SingleRecipeInput implements CustomRecipeInput{
    private final ItemStack input;
    private final boolean anyCustom;

    public SingleRecipeInput(ItemStack input) {
        this.input = input;
        this.anyCustom = CustomItems.isCustomStack(input);
    }

    @Override
    public ItemStack getItem(int index) {
        if (index != 0)
            throw new IllegalArgumentException("Recipe does not contain slot " + index);
        return input;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean anyCustom() {
        return this.anyCustom;
    }
}
