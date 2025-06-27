package de.cjdev.recipeapi.listener;

import de.cjdev.recipeapi.api.Helper;
import de.cjdev.recipeapi.RecipeAPI;
import de.cjdev.recipeapi.api.recipe.*;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.minecraft.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Crafter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.view.AnvilView;

import java.util.*;

public class CraftEventListener implements Listener {

    private static final ThreadLocal<Player> playerThreadLocal = new ThreadLocal<>();

    private static void onCraftHelper(ItemStack result, Player player) {
        if (RecipeAPI.Dependency.PaperModAPI.isEnabled()) {
            de.cjdev.papermodapi.api.item.CustomItem item = de.cjdev.papermodapi.api.item.CustomItems.getItemByStack(result);
            if (item != null)
                item.onCraftByPlayer(result, player.getWorld(), player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;
        playerThreadLocal.set(player);
        if (event.getCurrentItem() == null || event.getCurrentItem().isEmpty())
            return;
        if (!event.getCursor().isEmpty())
            return;
        if (event.getSlotType() != InventoryType.SlotType.RESULT)
            return;
        if (event.getClickedInventory() instanceof CraftingInventory craftingInventory) {
            RecipeAPI.LOGGER.info("Craft");
            event.getWhoClicked().sendMessage(Component.text("You (Player) CRAFTED!"));
            int repeat = 1;
            //if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
            //    for (ItemStack content : craftingInventory.getMatrix()) {
            //        if (content == null) continue;
            //        if (repeat < content.getAmount())
            //            repeat = content.getAmount();
            //    }
            //}
            ItemStack result = craftingInventory.getResult();

            // TODO: Make a Pull Request to PaperMC to allow for easy recipe remainders
            ItemStack[] matrix = craftingInventory.getMatrix();
            ItemStack[] copyMatrix = new ItemStack[matrix.length];
            //RecipeAPI.LOGGER.warning(String.valueOf(copyMatrix.length));
            for (int i = 0; i < matrix.length; i++) {
                ItemStack stack = matrix[i];
                if (stack == null)
                    continue;
                //ItemStack clonedStack = stack.clone();
                //for (int x = 0; x < repeat; x++) {
                if (!Helper.getRecipeRemainder(stack)) {
                    if (stack.getAmount() > 1)
                        stack.subtract();
                    continue;
                }
                //}
                //copyMatrix[i] = clonedStack;
            }
            //ItemStack[] giveResults = new ItemStack[repeat - 1];
            //for (int i = repeat - 1; i != -1; --i) {
                //if (i == 0) {
            onCraftHelper(result, player);
                    //continue;
                //}
                //ItemStack cloned = result.clone();
                //onCraftHelper(cloned, player);
                //giveResults[i - 1] = cloned;
            //}
            //player.getInventory().addItem(giveResults);

            //new BukkitRunnable() {
            //    @Override
            //    public void run() {
            //        craftingInventory.setMatrix(copyMatrix);
            //    }
            //}.runTask(RecipeAPI.getPlugin());
            event.setResult(Event.Result.DENY);
            Bukkit.getServer().broadcast(Component.text(String.join(", ", Arrays.stream(craftingInventory.getMatrix()).filter(Objects::nonNull).map(stack -> String.valueOf(stack.getAmount())).toList())));
        } else if (event.getClickedInventory() instanceof StonecutterInventory stoneCutterInventory) {
            ItemStack result = stoneCutterInventory.getResult();
            onCraftHelper(result, player);
        } else if (event.getClickedInventory() instanceof AnvilInventory anvilInventory) {
            ItemStack result = anvilInventory.getResult();
            onCraftHelper(result, player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrafterCraft(CrafterCraftEvent event) {
        for (ItemStack content : ((Crafter) event.getBlock().getState()).getInventory().getContents()) {
            if (!RecipeAPI.isStackCustom(content))
                continue;
            event.setCancelled(true);
            return;
        }
//        List<ItemStack> input = Arrays.stream(((Crafter)event.getBlock()).getInventory().getContents()).map(stack -> stack == null ? ItemStack.empty() : stack).toList();
//        CustomCraftingInput craftingInput = new CustomCraftingInput(3, 3, input);
//
//        for (CustomRecipe<?> customRecipe : RecipeAPI.CustomRecipes) {
//            if (!(customRecipe instanceof CustomCraftingRecipe craftingRecipe))
//                continue;
//            if (craftingRecipe.matches(craftingInput)) {
//                ItemStack result = craftingRecipe.assemble(craftingInput);
//                CustomItem item = CustomItems.getItemByStack(event.getResult());
//                if (item == null)
//                    return;
//                item.onCraft(result, event.getBlock().getWorld());
//                event.setResult(result);
//                return;
//            }
//        }
//
//        if (craftingInput.anyCustom())
//            event.setResult(ItemStack.empty());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        List<ItemStack> input = Arrays.stream(event.getInventory().getMatrix()).map(stack -> stack == null ? ItemStack.empty() : stack).toList();

        int width;
        int height = width = (int) Math.sqrt(input.size());

        CustomCraftingInput craftingInput = new CustomCraftingInput(width, height, input);

        Player player = playerThreadLocal.get();
        for (Map.Entry<NamespacedKey, CustomRecipe<?>> entry : RecipeAPI.CustomRecipes.entrySet()) {
            if (!player.hasDiscoveredRecipe(entry.getKey()) && Bukkit.getRecipe(entry.getKey()) != null) continue;
            if (!(entry.getValue() instanceof CustomCraftingRecipe craftingRecipe))
                continue;
            if (!craftingRecipe.matches(craftingInput))
                continue;
            event.getInventory().setResult(craftingRecipe.assemble(craftingInput));
            return;
        }

        if (craftingInput.anyCustom())
            event.getInventory().setResult(ItemStack.empty());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack baseItem = inventory.getFirstItem();

        CustomAnvilRecipeInput anvilInput = new CustomAnvilRecipeInput(baseItem, inventory.getSecondItem());
        AnvilView anvilView = event.getView();

        for (CustomRecipe<?> customRecipe : RecipeAPI.CustomRecipes.values()) {
            if (!(customRecipe instanceof CustomAnvilRecipe anvilRecipe))
                continue;
            if (!anvilRecipe.matches(anvilInput))
                continue;
            String itemName = anvilView.getRenameText();
            int cost = anvilInput.getCost();
            ItemStack stack = anvilRecipe.assemble(anvilInput);

            if (itemName != null && !StringUtil.isBlank(itemName)) {
                if (!itemName.equals(baseItem.getData(DataComponentTypes.CUSTOM_NAME))) {
                    cost += 1;
                    stack.setData(DataComponentTypes.CUSTOM_NAME, Component.text(anvilView.getRenameText()));
                }
            } else if (baseItem.hasData(DataComponentTypes.CUSTOM_NAME)) {
                cost += 1;
                stack.resetData(DataComponentTypes.CUSTOM_NAME);
            }
            event.setResult(stack);
            anvilView.setRepairCost(cost);
            return;
        }

        if (RecipeAPI.isStackCustom(inventory.getSecondItem()))
            event.setResult(ItemStack.empty());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareSmithing(PrepareSmithingEvent event) {
        SmithingInventory inventory = event.getInventory();
        CustomSmithingRecipeInput smithingInput = new CustomSmithingRecipeInput(inventory.getInputTemplate(), inventory.getInputEquipment(), inventory.getInputMineral());

        Player player = playerThreadLocal.get();
        for (Map.Entry<NamespacedKey, CustomRecipe<?>> entry : RecipeAPI.CustomRecipes.entrySet()) {
            if (!player.hasDiscoveredRecipe(entry.getKey()) && Bukkit.getRecipe(entry.getKey()) != null) continue;
            if (!(entry.getValue() instanceof CustomSmithingRecipe smithingRecipe))
                continue;
            if (!smithingRecipe.matches(smithingInput))
                continue;
            event.getInventory().setResult(smithingRecipe.assemble(smithingInput));
            return;
        }

        if (smithingInput.anyCustom())
            event.getInventory().setResult(ItemStack.empty());
    }
}
