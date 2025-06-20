package de.cjdev.recipeapi.listener;

import de.cjdev.recipeapi.Helper;
import de.cjdev.recipeapi.api.component.RecipeComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

public class FurnaceEventListener implements Listener {
    /// Calls when the Furnace wants to burn an item for fuel
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        Integer fuelTicks = RecipeComponents.FUEL.get(event.getFuel());
        if (fuelTicks == null)
            return;
        event.setBurnTime(fuelTicks);
        if (!Helper.getRecipeRemainder(event.getFuel()))
            return;
        event.setConsumeFuel(false);
    }

}
