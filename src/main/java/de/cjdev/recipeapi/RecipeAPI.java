package de.cjdev.recipeapi;

import com.google.gson.*;
import de.cjdev.recipeapi.api.component.RecipeComponents;
import de.cjdev.recipeapi.api.item.GenericItemInfo;
import de.cjdev.recipeapi.api.recipe.*;
import de.cjdev.recipeapi.listener.*;
import de.cjdev.recipeapi.serializer.*;
import de.cjdev.taglib.TagLib;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class RecipeAPI extends JavaPlugin {
    public static Logger LOGGER;
    public static final Gson RECIPE_PARSE_GSON;

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static boolean classesExist(String... classNames) {
        if (classNames.length == 0)
            return false;
        try {
            for (String className : classNames) {
                Class.forName(className);
            }
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public enum Dependency {
        PaperModAPI("de.cjdev.papermodapi.api.item.CustomItem", "de.cjdev.papermodapi.api.item.CustomItems"),
        TagLib("de.cjdev.taglib.TagLib");

        private final boolean enabled;

        Dependency(String className) {
            this.enabled = classExists(className);
        }

        Dependency(String... classNames) {
            this.enabled = classesExist(classNames);
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    public static final Map<NamespacedKey, CustomRecipe<?>> CustomRecipes = new HashMap<>();
    private static final HashMap<NamespacedKey, Class<? extends RecipeSerializer>> recipeSerializers = new HashMap<>();

    public static boolean itemTagContains(NamespacedKey tagKey, de.cjdev.papermodapi.api.item.CustomItem customItem) {
        return customItem.getId() != null && itemTagContains(tagKey, customItem.getId());
    }

    public static boolean itemTagContains(NamespacedKey tagKey, @NotNull NamespacedKey itemId) {
        if (!TagLib.ITEM_TAGS.containsKey(tagKey)) return false;
        return TagLib.ITEM_TAGS.get(tagKey).contains(itemId);
    }

    public static <T extends RecipeSerializer> void registerRecipeSerializer(NamespacedKey type, Class<T> serializer) {
        recipeSerializers.put(type, serializer);
    }

    @Override
    public void onLoad() {
        // Registering Recipe Types
        registerRecipeSerializer(NamespacedKey.minecraft("crafting_shapeless"), CraftingShapelessSerializer.class);
        registerRecipeSerializer(NamespacedKey.minecraft("crafting_shaped"), CraftingShapedSerializer.class);
        registerRecipeSerializer(NamespacedKey.minecraft("crafting_transmute"), CraftingTransmuteSerializer.class);
        // REGION not rn
        registerRecipeSerializer(NamespacedKey.minecraft("blasting"), BlastingSerializer.class);
        registerRecipeSerializer(NamespacedKey.minecraft("smelting"), SmeltingSerializer.class);
        registerRecipeSerializer(NamespacedKey.minecraft("smoking"), SmokingSerializer.class);
        registerRecipeSerializer(NamespacedKey.minecraft("campfire_cooking"), CampfireCookingSerializer.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        LOGGER = getLogger();

        // Registering Special Recipes
        addRecipe(new NamespacedKey("recipeapi", "repair"), new CustomRepairItemRecipe());

        if (Dependency.PaperModAPI.isEnabled()) {
            RecipeComponents.load();
            addRecipe(new NamespacedKey("recipeapi", "repair_anvil"), new CustomAnvilRepairItemRecipe());
            addRecipe(new NamespacedKey("recipeapi", "dye_item"), new CustomDyeItemRecipe());
        }

        // Registering Event Listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new CraftEventListener(this), this);
        pluginManager.registerEvents(new CookEventListener(), this);
        pluginManager.registerEvents(new FurnaceEventListener(), this);
        pluginManager.registerEvents(new InventoryClickEvent(), this);

        // region HIGHLY EXPERIMENTAL
        //pluginManager.registerEvents(new EnchantEventListener(), this);
        //if (Dependency.MorePaperEvents.isEnabled()) {
        //    pluginManager.registerEvents(new IgniteFurnaceEventListener(), this);
        //    pluginManager.registerEvents(new PlayerInteractEventListener(), this);
        //    LOGGER.info("\u001B[38;2;85;255;85mFurnace Features available\u001B[0m");
        //}

        new BukkitRunnable() {
            @Override
            public void run() {
                loadRecipesFromData();
            }
        }.runTask(this);
    }

    public static Supplier<Set<NamespacedKey>> getTagSupplier(NamespacedKey tagKey) {
        if (!Dependency.TagLib.isEnabled())
            return Set::of;
        return () -> de.cjdev.taglib.TagLib.ITEM_TAGS.getOrDefault(tagKey, Set.of());
    }

    public static boolean isStackCustom(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return false;
        return getItemInfo(stack).custom();
    }

    public static @NotNull GenericItemInfo getItemInfo(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) return new GenericItemInfo(true, NamespacedKey.minecraft("air"), false, false);
        if (Dependency.PaperModAPI.isEnabled()) {
            NamespacedKey itemId = de.cjdev.papermodapi.api.item.CustomItems.getKeyByStack(stack);
            de.cjdev.papermodapi.api.item.CustomItem customItem = de.cjdev.papermodapi.api.item.CustomItems.getItemByKey(itemId);
            if (customItem != null)
                return new GenericItemInfo(false, itemId, true, customItem.isDyeable());
        }
        return new GenericItemInfo(false, stack.getType().getKey(), false, Tag.ITEMS_DYEABLE.isTagged(stack.getType()));
    }

    public static NamespacedKey getKeyFromStack(@Nullable ItemStack stack) {
        return getItemInfo(stack).itemId();
    }

    public static ItemStack parseItemId(NamespacedKey id) {
        if (Dependency.PaperModAPI.isEnabled()) {
            de.cjdev.papermodapi.api.item.CustomItem customResultItem = de.cjdev.papermodapi.api.item.CustomItems.getItemByKey(id);
            if (customResultItem != null)
                return customResultItem.getDefaultStack();
        }
        ItemType vanillaResultItem = Registry.ITEM.get(id);
        return vanillaResultItem != null ? vanillaResultItem.createItemStack() : ItemStack.empty();
    }

    public static void addDataFromZipEntries(ZipFile zipFile) {
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

        String regex = "^data/([^/]+)/([^/]+)/(.+)\\.json$";
        Pattern pattern = Pattern.compile(regex);
        Gson GSON = new Gson();

        while (zipEntries.hasMoreElements()) {
            ZipEntry zipEntry = zipEntries.nextElement();

            Matcher matcher = pattern.matcher(zipEntry.getName());

            if (!zipEntry.isDirectory() && matcher.find()) {
                String namespace = matcher.group(1);
                String type = matcher.group(2);
                String fileName = matcher.group(3);

                if (!type.equals("recipe")) continue;
                try (InputStreamReader is = new InputStreamReader(zipFile.getInputStream(zipEntry))) {
                    JsonObject json = GSON.fromJson(is, JsonObject.class);

                    JsonElement recipeTypeNode = json.get("type");
                    NamespacedKey serializerId;
                    if (recipeTypeNode == null || (serializerId = NamespacedKey.fromString(recipeTypeNode.getAsString())) == null) {
                        warnFailLoad("%s:%s/%s".formatted(namespace, type, fileName));
                        continue;
                    }

                    if (!recipeSerializers.containsKey(serializerId))
                        LOGGER.warning("Unknown Recipe Type '" + serializerId.asString() + "'");
                    else {
                        Class<? extends RecipeSerializer> serializerClass = recipeSerializers.get(serializerId);
                        RecipeSerializer serializer = RECIPE_PARSE_GSON.fromJson(json, serializerClass);
                        CustomRecipe<?> customRecipe = serializer.asRecipe();
                        if (customRecipe != null)
                            addRecipe(new NamespacedKey(namespace, fileName), customRecipe);
                        else
                            warnFailLoad("%s:%s/%s".formatted(namespace, type, fileName));
                    }
                } catch (IllegalArgumentException | IOException e) {
                    warnFailLoad("%s:%s/%s".formatted(namespace, type, fileName), e.getMessage());
                }
            }
        }
    }

    private static void warnFailLoad(String id, String reason) {
        LOGGER.warning("\u001B[38;2;255;85;85m%s failed to load: %s\u001B[0m".formatted(id, reason));
    }

    private static void warnFailLoad(String id) {
        LOGGER.warning("\u001B[38;2;255;85;85m%s failed to load\u001B[0m".formatted(id));
    }

    public static void loadRecipesFromData() {
        for (@NotNull Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            URL classUrl = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
            if (classUrl == null)
                continue;

            try {
                Path path = Paths.get(classUrl.toURI());

                ZipFile jarFile = new ZipFile(path.toFile());
                addDataFromZipEntries(jarFile);
            } catch (URISyntaxException ignored) {
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    public static @Nullable Recipe addRecipe(NamespacedKey key, CustomRecipe<?> recipe) {
        if (CustomRecipes.put(key, recipe) != null) return null;
        Recipe bukkitRecipe = recipe.toBukketRecipe(key);
        if (bukkitRecipe == null) return null;
        Bukkit.removeRecipe(key);
        Bukkit.addRecipe(bukkitRecipe);
        return bukkitRecipe;
    }

    public static NamespacedKey key(String id){
        return new NamespacedKey("recipeapi", id);
    }

    public record ResultStack(@NotNull ItemStack handle) {
        public static ResultStack empty() {
            return new ResultStack(ItemStack.empty());
        }
    }

    public static class IngredientDeserializer implements JsonDeserializer<CustomIngredient> {
        @Override
        public CustomIngredient deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                String plainId = json.getAsJsonPrimitive().getAsString();
                boolean isTag = plainId.startsWith("#");
                NamespacedKey material = NamespacedKey.fromString(plainId.substring(isTag ? 1 : 0));
                if (material == null)
                    return new CustomIngredient();
                return new CustomIngredient(isTag ? getTagSupplier(material) : () -> Set.of(material));
            } else if (json.isJsonArray()) {
                JsonArray obj = json.getAsJsonArray();
                Set<NamespacedKey> ingredients = new HashSet<>();
                for (JsonElement jsonElement : obj) {
                    if (!jsonElement.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) continue;
                    NamespacedKey id = NamespacedKey.fromString(json.getAsJsonPrimitive().getAsString());
                    if (id == null) continue;
                    ingredients.add(id);
                }
                return new CustomIngredient(ingredients);
            }
            return new CustomIngredient();
        }
    }

    public static class ResultItemDeserializer implements JsonDeserializer<ResultStack> {
        @Override
        public ResultStack deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString())
                return new ResultStack(parseItemId(NamespacedKey.fromString(json.getAsString())));
            else if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                JsonElement Id = obj.get("id");
                ItemStack resultStack = Id != null && Id.isJsonPrimitive() && Id.getAsJsonPrimitive().isString() ? parseItemId(NamespacedKey.fromString(Id.getAsJsonPrimitive().getAsString())) : ItemStack.empty();
                JsonElement cId = obj.get("count");
                int count = cId != null && cId.isJsonPrimitive() && cId.getAsJsonPrimitive().isNumber() ? cId.getAsInt() : 1;
                return new ResultStack(resultStack.asQuantity(count));
            }
            return ResultStack.empty();
        }
    }

    static {
        RECIPE_PARSE_GSON = new GsonBuilder()
                .registerTypeAdapter(ResultStack.class, new ResultItemDeserializer())
                .registerTypeAdapter(CustomIngredient.class, new IngredientDeserializer())
                .create();
    }

}
