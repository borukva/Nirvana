package galena.nirvana.data;

import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;

public class ModTags {
    public static final TagKey<Item> SEEDS = getTag(BuiltInRegistries.ITEM, "seeds");
    public static final TagKey<Item> SMOKING_ITEM = getModTag(BuiltInRegistries.ITEM, "smoking_item");
    public static final TagKey<Item> NAUSEATING = getModTag(BuiltInRegistries.ITEM, "nauseating");

    private static <T> TagKey<T> getTag(Registry<T> registry, String path) {
        return TagKey.create(registry.key(), Identifier.fromNamespaceAndPath("c", path));
    }

    private static <T> TagKey<T> getModTag(Registry<T> registry, String path) {
        return TagKey.create(registry.key(), Identifier.fromNamespaceAndPath("nirvana", path));
    }

    public static void register() {}
}
