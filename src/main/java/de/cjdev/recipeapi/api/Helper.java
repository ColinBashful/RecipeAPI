package de.cjdev.recipeapi.api;

import de.cjdev.recipeapi.RecipeAPI;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import org.bukkit.Color;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
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

    public static void applyColors(ItemStack stack, List<Color> colors) {
        int i = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        net.minecraft.world.item.component.DyedItemColor dyedItemColor = CraftItemStack.unwrap(stack).get(DataComponents.DYED_COLOR);
        if (dyedItemColor != null) {
            int i5 = ARGB.red(dyedItemColor.rgb());
            int i6 = ARGB.green(dyedItemColor.rgb());
            int i7 = ARGB.blue(dyedItemColor.rgb());
            i3 += Math.max(i5, Math.max(i6, i7));
            i += i5;
            i1 += i6;
            i2 += i7;
            ++i4;
        }

        for (Color color : colors) {
            int i7 = color.asARGB();
            int i8 = ARGB.red(i7);
            int i9 = ARGB.green(i7);
            int i10 = ARGB.blue(i7);
            i3 += Math.max(i8, Math.max(i9, i10));
            i += i8;
            i1 += i9;
            i2 += i10;
            ++i4;
        }

        int i5 = i / i4;
        int i6 = i1 / i4;
        int i7 = i2 / i4;
        float f = (float) i3 / (float) i4;
        float f1 = (float) Math.max(i5, Math.max(i6, i7));
        i5 = (int) ((float) i5 * f / f1);
        i6 = (int) ((float) i6 * f / f1);
        i7 = (int) ((float) i7 * f / f1);
        int i10 = ARGB.color(0, i5, i6, i7);
        CraftItemStack.unwrap(stack).set(DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(i10));
    }
}
