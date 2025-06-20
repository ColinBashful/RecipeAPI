package de.cjdev.recipeapi.api.recipe;

import de.cjdev.papermodapi.api.item.CustomItem;
import de.cjdev.papermodapi.api.item.CustomItems;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CustomRepairItemRecipe implements CustomCraftingRecipe {
    public CustomRepairItemRecipe() {
    }

    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CustomCraftingInput craftingInput) {
        ItemStack itemStack = craftingInput.ingredients().getFirst();
        ItemStack itemStack1 = craftingInput.ingredients().getLast();
        Damageable damageable = (Damageable) itemStack.getItemMeta();
        Damageable damageable1 = (Damageable) itemStack1.getItemMeta();
        int max = Math.max(damageable.getMaxDamage(), damageable1.getMaxDamage());
        int i = damageable.getMaxDamage() - damageable.getDamage();
        int i1 = damageable1.getMaxDamage() - damageable1.getDamage();
        int i2 = i + i1 + max * 5 / 100;
        ItemStack itemStack2 = CustomItems.getItemByStack(itemStack).getDefaultStack();
        Damageable damageable2 = (Damageable) itemStack2.getItemMeta();
        damageable2.setMaxDamage(max);
        damageable2.setDamage(Math.max(max - i2, 0));
        itemStack2.setItemMeta(damageable2);
        ItemEnchantments enchantments = itemStack.getData(DataComponentTypes.ENCHANTMENTS);
        ItemEnchantments enchantments1 = itemStack1.getData(DataComponentTypes.ENCHANTMENTS);
        boolean hasCurses = false;
        Map<Enchantment, Integer> cursedEnchantments = new HashMap<>();
        if (enchantments != null)
            for (Enchantment enchantment : enchantments.enchantments().keySet()) {
                if (!enchantment.isCursed())
                    continue;
                hasCurses = true;
                cursedEnchantments.put(enchantment, enchantments.enchantments().get(enchantment));
            }
        if (enchantments1 != null)
            for (Enchantment enchantment : enchantments1.enchantments().keySet()) {
                if (!enchantment.isCursed())
                    continue;
                hasCurses = true;
                cursedEnchantments.compute(enchantment, (enchantment1, integer) -> {
                    int level = enchantments.enchantments().get(enchantment);
                    if (integer == null)
                        return level;
                    return Math.max(integer, level);
                });
            }
        if (hasCurses)
            itemStack2.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments().addAll(cursedEnchantments).build());
        return itemStack2;
    }

    @Override
    public boolean matches(@NotNull CustomCraftingInput input) {
        if (input.ingredientCount() != 2)
            return false;
        return canCombine(input.ingredients().getFirst(), input.ingredients().getLast());
    }

    public static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return CustomItem.isSimilar(stack2, stack1) && stack1.getAmount() == 1 && stack2.getAmount() == 1 && stack1.hasData(DataComponentTypes.MAX_DAMAGE) && stack2.hasData(DataComponentTypes.MAX_DAMAGE) && stack1.hasData(DataComponentTypes.DAMAGE) && stack2.hasData(DataComponentTypes.DAMAGE);
    }

    @Override
    public Recipe toBukketRecipe(@NotNull NamespacedKey key) {
        return null;
    }
}
