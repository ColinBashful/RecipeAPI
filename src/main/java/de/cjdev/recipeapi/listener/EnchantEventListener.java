package de.cjdev.recipeapi.listener;

import de.cjdev.papermodapi.api.item.CustomItem;
import de.cjdev.papermodapi.api.item.CustomItems;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class EnchantEventListener implements Listener {

    @EventHandler
    public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
        CustomItem customItem = CustomItems.getItemByStack(event.getItem());
        if (customItem == null)
            return;
        event.getOffers()[0] = new EnchantmentOffer(Enchantment.SHARPNESS, 5, 1);//EnchantmentMenu
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {

    }
}
