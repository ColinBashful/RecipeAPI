package de.cjdev.recipeapi.api.item;

import org.bukkit.NamespacedKey;

public record GenericItemInfo(boolean air, NamespacedKey itemId, boolean custom, boolean dyeable) {
}
