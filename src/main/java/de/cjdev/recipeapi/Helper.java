package de.cjdev.recipeapi;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.function.Consumer;

public class Helper {

    private Helper() {
    }

    /**
     * @param stack Stack of which the Recipe Remainder is got
     * @return Whether anything changed
     */
    public static boolean getRecipeRemainder(ItemStack stack) {
        SUBTRACT:
        {
            PaperModAPI:
            {
                if (RecipeAPI.Dependency.PaperModAPI.isEnabled()) {
                    de.cjdev.papermodapi.api.item.CustomItem customIngredient = de.cjdev.papermodapi.api.item.CustomItems.getItemByStack(stack);
                    if (customIngredient == null)
                        break PaperModAPI;
                    Consumer<ItemStack> recipeRemainder = customIngredient.getRecipeRemainder();
                    if (recipeRemainder == null)
                        break SUBTRACT;
                    recipeRemainder.accept(stack);
                    return true;
                }
            }
        }
        return false;
    }
}
