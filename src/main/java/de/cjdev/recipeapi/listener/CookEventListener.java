package de.cjdev.recipeapi.listener;

import de.cjdev.papermodapi.api.item.CustomItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;

public class CookEventListener implements Listener {
    @EventHandler
    public void onBlockCook(BlockCookEvent event){
        // TODO: Make it work // NOTE: this is also called when a furnace smelts
        //if (CustomItems.getItemByStack(event.getSource()) == null) return;
        //event.setCancelled(true);
    }
}
