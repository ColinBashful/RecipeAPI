package de.cjdev.recipeapi.api.recipe;

import de.cjdev.recipeapi.RecipeAPI;
import de.cjdev.recipeapi.api.component.RecipeComponents;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomAnvilRepairItemRecipe implements CustomAnvilRecipe {
    @Override
    public @Nullable CustomIngredient baseIngredient() {
        return null;
    }

    @Override
    public @Nullable CustomIngredient additionIngredient() {
        return null;
    }

    @Override
    public boolean matches(@NotNull CustomAnvilRecipeInput craftingInput) {
        if (!(craftingInput.base() instanceof Damageable))
            return false;
        NamespacedKey repairableBy = RecipeComponents.REPAIRABLE.get(craftingInput.base());
        if (repairableBy == null)
            return false;
        return RecipeAPI.getKeyFromStack(craftingInput.addition()).equals(repairableBy);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CustomAnvilRecipeInput craftingInput) {
        ItemStack result = craftingInput.base().clone();
        Damageable damageable = (Damageable) result.getItemMeta();
        damageable.resetDamage();
        result.setItemMeta(damageable);
        return result;
    }

    @Override
    public @Nullable Recipe toBukketRecipe(NamespacedKey key) {
        return null;
    }
}
