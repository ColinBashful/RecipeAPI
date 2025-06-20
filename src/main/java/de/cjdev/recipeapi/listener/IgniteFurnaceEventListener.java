package de.cjdev.recipeapi.listener;

import de.cjdev.papermodapi.api.item.CustomItems;
import de.cjdev.recipeapi.RecipeAPI;
import de.cjdev.recipeapi.api.recipe.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

import java.util.Map;

public class IgniteFurnaceEventListener implements Listener {
//    @EventHandler
//    public void onFurnacePrepareSmelt(FurnacePrepareSmeltEvent event) {
//        CustomCookingRecipeInput cookingInput = new CustomCookingRecipeInput(event.getFuel(), event.getSource());
//
//        for (CustomRecipe<?> customRecipe : RecipeAPI.CustomRecipes) {
//            AbstractCookingRecipe cookingRecipe;
//            switch (event.getBlock().getBlockData().getMaterial()) {
//                case FURNACE -> {
//                    if (!(customRecipe instanceof CustomSmeltingRecipe recipe))
//                        continue;
//                    cookingRecipe = recipe;
//                }
//                case BLAST_FURNACE -> {
//                    if (!(customRecipe instanceof CustomBlastingRecipe recipe))
//                        continue;
//                    cookingRecipe = recipe;
//                }
//                case SMOKER -> {
//                    if (!(customRecipe instanceof CustomSmokingRecipe recipe))
//                        continue;
//                    cookingRecipe = recipe;
//                }
//                default -> {
//                    continue;
//                }
//            }
//
//            if (cookingRecipe.matches(cookingInput)) {
//                event.setCanSmelt(true);
//                return;
//            }
//        }
//
//        if (cookingInput.isCustom())
//            event.setCancelled(true);
//    }

    @EventHandler
    public void onFurnaceStartSmelt(FurnaceStartSmeltEvent event) {
        if (CustomItems.isCustomStack(event.getSource())) {
            event.setTotalCookTime(Integer.MAX_VALUE);
        }

        //for (CustomRecipe<?> customRecipe : RecipeAPI.CustomRecipes) {
        //    AbstractCookingRecipe cookingRecipe;
        //    switch (event.getBlock().getBlockData().getMaterial()) {
        //        case FURNACE -> {
        //            if (!(customRecipe instanceof CustomSmeltingRecipe recipe))
        //                continue;
        //            cookingRecipe = recipe;
        //        }
        //        case BLAST_FURNACE -> {
        //            if (!(customRecipe instanceof CustomBlastingRecipe recipe))
        //                continue;
        //            cookingRecipe = recipe;
        //        }
        //        case SMOKER -> {
        //            if (!(customRecipe instanceof CustomSmokingRecipe recipe))
        //                continue;
        //            cookingRecipe = recipe;
        //        }
        //        default -> {
        //            continue;
        //        }
        //    }
        //
        //    if (cookingRecipe.matches(cookingInput)) {
        //        event.setTotalCookTime(cookingRecipe.getCookingTime());
        //        return;
        //    }
        //}
        //
        //NamespacedKey recipeKey = event.getRecipe().getKey();
        //if (cookingInput.anyCustom() || (recipeKey.namespace().equals("recipeapi") && recipeKey.value().endsWith("_fix")))
        //    event.setTotalCookTime(Integer.MAX_VALUE);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (RecipeAPI.isStackCustom(event.getSource()))
            event.setCancelled(true);
        //if(!(event.getBlock().getState() instanceof Furnace furnace)) return;
        //CustomCookingRecipeInput cookingInput = new CustomCookingRecipeInput(furnace.getInventory().getFuel(), event.getSource());
        //
        //for (CustomRecipe<?> customRecipe : RecipeAPI.CustomRecipes) {
        //    AbstractCookingRecipe cookingRecipe;
        //    switch (event.getBlock().getBlockData().getMaterial()) {
        //        case FURNACE -> {
        //            if (!(customRecipe instanceof CustomSmeltingRecipe recipe))
        //                continue;
        //            cookingRecipe = recipe;
        //        }
        //        case BLAST_FURNACE -> {
        //            if (!(customRecipe instanceof CustomBlastingRecipe recipe))
        //                continue;
        //            cookingRecipe = recipe;
        //        }
        //        case SMOKER -> {
        //            if (!(customRecipe instanceof CustomSmokingRecipe recipe))
        //                continue;
        //            cookingRecipe = recipe;
        //        }
        //        default -> {
        //            continue;
        //        }
        //    }
        //
        //    if (cookingRecipe.matches(cookingInput)) {
        //        event.setResult(cookingRecipe.assemble(cookingInput));
        //        return;
        //    }
        //}
        //
        ////RecipeAPI.LOGGER.warning("empty");
        //if (cookingInput.anyCustom())
        //    event.setResult(ItemStack.empty());
    }
}
