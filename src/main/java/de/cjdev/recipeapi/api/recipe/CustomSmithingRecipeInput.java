package de.cjdev.recipeapi.api.recipe;

import de.cjdev.recipeapi.RecipeAPI;
import org.bukkit.inventory.ItemStack;

public class CustomSmithingRecipeInput implements CustomRecipeInput {
    private final ItemStack template;
    private final ItemStack base;
    private final ItemStack addition;
    private final boolean anyCustom;

    public CustomSmithingRecipeInput(ItemStack template, ItemStack base, ItemStack addition) {
        this.template = template == null ? ItemStack.empty() : template;
        this.base = base == null ? ItemStack.empty() : base;
        this.addition = addition == null ? ItemStack.empty() : addition;

        this.anyCustom = RecipeAPI.isStackCustom(template) || RecipeAPI.isStackCustom(base) || RecipeAPI.isStackCustom(addition);
    }

    public boolean anyCustom(){
        return this.anyCustom;
    }

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> this.template;
            case 1 -> this.base;
            case 2 -> this.addition;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + index);
        };
    }

    @Override
    public int size() {
        return 3;
    }

    public boolean isEmpty() {
        return this.template.isEmpty() && this.base.isEmpty() && this.addition.isEmpty();
    }

    public ItemStack template() {
        return this.template;
    }

    public ItemStack base() {
        return this.base;
    }

    public ItemStack addition() {
        return this.addition;
    }
}
