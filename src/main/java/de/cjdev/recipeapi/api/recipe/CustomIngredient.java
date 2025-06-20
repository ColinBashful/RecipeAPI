package de.cjdev.recipeapi.api.recipe;

import de.cjdev.recipeapi.RecipeAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CustomIngredient implements Predicate<ItemStack>, Cloneable {
    static boolean testOptionalIngredient(CustomIngredient ingredient, @NotNull ItemStack stack) {
        return (ingredient != null && ingredient.test(stack)) || stack.isEmpty();
    }

    public final Supplier<Set<NamespacedKey>> keysSupplier;

    public CustomIngredient(Set<NamespacedKey> keys) {
        this.keysSupplier = () -> keys;
    }

    public CustomIngredient(Supplier<Set<NamespacedKey>> keysSupplier) {
        this.keysSupplier = keysSupplier;
    }

    public CustomIngredient(NamespacedKey... keys) {
        this(Set.of(keys));
    }

    public CustomIngredient(de.cjdev.papermodapi.api.item.CustomItem customItem) {
        this(customItem.getId());
    }

    public CustomIngredient(Material material) {
        this(material.getKey());
    }

    public boolean test(ItemStack stack) {
        return this.keysSupplier.get().contains(RecipeAPI.getKeyFromStack(stack));
    }

    public RecipeChoice toBukkitRecipeChoice() {
        Set<NamespacedKey> keys = this.keysSupplier.get();
        List<ItemStack> stacks = new ArrayList<>(this.keysSupplier.get().size());
        for (NamespacedKey key : keys) {
            if (RecipeAPI.Dependency.PaperModAPI.isEnabled()) {
                de.cjdev.papermodapi.api.item.CustomItem customItem = de.cjdev.papermodapi.api.item.CustomItems.getItemByKey(key);
                if (customItem != null) {
                    stacks.add(customItem.getDefaultStack());
                    continue;
                }
            }
            Material material = Material.getMaterial(key.value().toUpperCase(Locale.ROOT));
            stacks.add(material == null ? ItemStack.empty() : ItemStack.of(material));
        }
        return keys.isEmpty() ? new RecipeChoice.MaterialChoice(Material.BARRIER) : new RecipeChoice.ExactChoice(stacks);
    }

    @Override
    public @NotNull CustomIngredient clone() {
        return new CustomIngredient(this.keysSupplier);
    }
}
