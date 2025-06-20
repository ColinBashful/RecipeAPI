package de.cjdev.recipeapi.api.item;

import de.cjdev.papermodapi.api.item.CustomItem;
import de.cjdev.recipeapi.api.component.RecipeComponents;
import org.bukkit.NamespacedKey;

public class RecipeItemSettings extends CustomItem.Settings {

    public final RecipeItemSettings fuel(int fuelTicks) {
        this.component(RecipeComponents.FUEL, fuelTicks);
        return this;
    }

    public final RecipeItemSettings repairable(NamespacedKey repairIngredient) {
        this.component(RecipeComponents.REPAIRABLE, repairIngredient);
        return this;
    }
}
