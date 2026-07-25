package galena.nirvana.data;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Item> SEEDS = getTag(Registries.ITEM, "seeds");
    public static final TagKey<Item> SMOKING_ITEM = getModTag(Registries.ITEM, "smoking_item");
    public static final TagKey<Item> NAUSEATING = getModTag(Registries.ITEM, "nauseating");

    private static <T> TagKey<T> getTag(Registry<T> registry, String path) {
        return TagKey.of(registry.getKey(), Identifier.of("c", path));
    }

    private static <T> TagKey<T> getModTag(Registry<T> registry, String path) {
        return TagKey.of(registry.getKey(), Identifier.of("nirvana", path));
    }

    public static void register() {}
}
