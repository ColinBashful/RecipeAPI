package de.cjdev.recipeapi.api.recipe;

import de.cjdev.papermodapi.api.item.CustomItem;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomSmithingTransformRecipe implements CustomSmithingRecipe {
    private final CustomIngredient template;
    private final CustomIngredient base;
    private final CustomIngredient addition;
    private final ItemStack result;

    final boolean copyDataComponents;

    public CustomSmithingTransformRecipe(@Nullable CustomIngredient template, @Nullable CustomIngredient base, @Nullable CustomIngredient addition, @NotNull ItemStack result, boolean copyDataComponents) {
        this.copyDataComponents = copyDataComponents;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public CustomSmithingTransformRecipe(@Nullable CustomIngredient template, @Nullable CustomIngredient base, @Nullable CustomIngredient addition, @NotNull CustomItem result, boolean copyDataComponents) {
        this(template, base, addition, result.getDefaultStack(), copyDataComponents);
    }

    public CustomSmithingTransformRecipe(@Nullable CustomIngredient template, @Nullable CustomIngredient base, @Nullable CustomIngredient addition, @NotNull ItemStack result){
        this(template, base, addition, result, true);
    }

    public CustomSmithingTransformRecipe(@Nullable CustomIngredient template, @Nullable CustomIngredient base, @Nullable CustomIngredient addition, @NotNull CustomItem result){
        this(template, base, addition, result, true);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CustomSmithingRecipeInput smithingInput) {
        net.minecraft.world.item.ItemStack nmsResult = CraftItemStack.unwrap(this.result);
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.unwrap(smithingInput.base()).transmuteCopy(nmsResult.getItem(), nmsResult.getCount());
        if (this.copyDataComponents) {
            itemStack.applyComponents(nmsResult.getComponentsPatch());
        }

        return itemStack.getBukkitStack();
    }

    @Override
    public @Nullable CustomIngredient templateIngredient() {
        return this.template;
    }

    @Override
    public @Nullable CustomIngredient baseIngredient() {
        return this.base;
    }

    @Override
    public @Nullable CustomIngredient additionIngredient() {
        return this.addition;
    }

    @Override
    public @Nullable Recipe toBukketRecipe(NamespacedKey key) {
        return null;
    }
}
