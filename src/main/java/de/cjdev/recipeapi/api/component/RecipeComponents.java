package de.cjdev.recipeapi.api.component;

import com.mojang.serialization.Codec;
import de.cjdev.papermodapi.api.component.CustomDataComponent;
import de.cjdev.papermodapi.api.component.MoreCodecs;
import de.cjdev.recipeapi.RecipeAPI;
import net.minecraft.util.Unit;
import org.bukkit.NamespacedKey;

public final class RecipeComponents {
    public static final CustomDataComponent<Integer> FUEL = new CustomDataComponent<>(RecipeAPI.key("fuel"), Codec.INT);
    public static final CustomDataComponent<Unit> DYEABLE = new CustomDataComponent<>(RecipeAPI.key("dyeable"), Unit.CODEC);
    public static final CustomDataComponent<NamespacedKey> REPAIRABLE = new CustomDataComponent<>(RecipeAPI.key("repairable"), MoreCodecs.NAMESPACEDKEY);

    public static void load() {}
}
