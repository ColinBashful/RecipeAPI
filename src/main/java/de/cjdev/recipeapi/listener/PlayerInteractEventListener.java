package de.cjdev.recipeapi.listener;

//import de.cjdev.morepaperevents.api.event.CampfirePlaceItemEvent;
import de.cjdev.recipeapi.RecipeAPI;
import de.cjdev.recipeapi.api.recipe.AbstractCookingRecipe;
import de.cjdev.recipeapi.api.recipe.CustomCookingRecipeInput;
import de.cjdev.recipeapi.api.recipe.CustomRecipe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerInteractEventListener implements Listener {
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onCampfirePlaceItem(CampfirePlaceItemEvent event) {
//        CustomCookingRecipeInput cookingRecipeInput = new CustomCookingRecipeInput(null, event.getPlaceStack());
//        for (CustomRecipe<?> customRecipe : RecipeAPI.CustomRecipes) {
//            if (!(customRecipe instanceof AbstractCookingRecipe customCookingRecipe))
//                continue;
//            if (customCookingRecipe.matches(cookingRecipeInput)) {
//                event.allowPlacing(true);
//                return;
//            }
//        }
//        if (cookingRecipeInput.isCustom())
//            event.allowPlacing(false);
//    }
}
