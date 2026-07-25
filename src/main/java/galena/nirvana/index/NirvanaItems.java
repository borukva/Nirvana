package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.utils.ModFoodComponents;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.function.Function;
public class NirvanaItems {
    public static final Item HEMP = registerItem("hemp", SimplePolymerItem::new, new Item.Settings());
    public static final Item WEED = registerItem("weed", SimplePolymerItem::new, new Item.Settings());
    public static Item HEMP_SEEDS = registerItem("hemp_seeds",  settings -> new PolymerBlockItem(NirvanaBlocks.HEMP, settings));
    public static Item WILD_HEMP = registerItem("wild_hemp",  settings -> new PolymerBlockItem(NirvanaBlocks.WILD_HEMP, settings));
    public static final Item WEED_BROWNIE  = registerItem("weed_brownie", SimplePolymerItem::new, new Item.Settings().food(new FoodComponent(2, 0.1f, false), ModConsumableComponents.BROWNIE));
    public static final Item HEMP_CLOTH = registerItem("hemp_cloth", SimplePolymerItem::new, new Item.Settings());

    public static final Item POTION_BONG = registerItem("potion_bong",
            settings -> new PotionBongItem(settings.maxDamage(4)));

    public static final Item PEACE_BANNER_PATTERN = registerItem("peace_banner_pattern", SimplePolymerItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)
                    .component(DataComponentTypes.PROVIDES_BANNER_PATTERNS, TagKey.of(RegistryKeys.BANNER_PATTERN, Identifier.of(Nirvana.MOD_ID, "peace_banner_patterns"))));

    public static final Item JOINT = registerItem(
            "joint",
            settings -> new SmokingItem(settings.maxDamage(3), List.of(
                    new StatusEffectInstance(NirvanaEffects.PEACE, 400, 0)
            ), "joint", ItemStack.EMPTY, NirvanaSounds.SMOKING)
    );

    public static final Item BONG = registerItem(
            "bong",
            settings -> new SmokingItem(settings.maxDamage(4), List.of(
                    new StatusEffectInstance(NirvanaEffects.PEACE, 600, 0)
            ), "bong", new ItemStack(Items.GLASS_BOTTLE), NirvanaSounds.BONG)
    );

    public static final Item TEST_POTION_BONG = registerItem("test_potion_bong", TestPotionBongItem::new, new Item.Settings());

    public static final Item REEFER_SPAWN_EGG = registerItem("reefer_spawn_egg", ReeferSpawnEgg::new, new Item.Settings());
    public static final Item THC_MINECART = registerItem("thc_minecart", ThcMinecartItem::new, new Item.Settings().maxCount(1));

    public static final Item OLD_PIPE = registerItem("old_pipe", SimplePolymerItem::new, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    public static final Item STUFFED_PIPE = registerItem(
            "stuffed_pipe",
            settings -> new SmokingItem(settings.maxDamage(6).rarity(Rarity.UNCOMMON), List.of(
                    new StatusEffectInstance(NirvanaEffects.PEACE, 600, 0)
            ), "stuffed_pipe", new ItemStack(NirvanaItems.OLD_PIPE), NirvanaSounds.SMOKING)
    );

    public static final Item SUSPICIOUS_PIPE = registerItem(
            "suspicious_pipe",
            settings -> new SuspiciousPipeItem(settings.maxDamage(6).rarity(Rarity.UNCOMMON))
    );

    public static final Item HERBAL_SALVE = registerItem(
            "herbal_salve",
            settings -> new HerbalSalveItem(settings.maxCount(1), new ItemStack(Items.BOWL))
    );

    // Archived per request - a salve that gets someone else high on contact. May come back later.
    // public static final Item PEACE_SALVE = registerItem(
    //         "peace_salve",
    //         settings -> new SalveItem(settings.maxCount(1), List.of(
    //                 new StatusEffectInstance(NirvanaEffects.PEACE, 600, 0)
    //         ), "peace_salve", new ItemStack(Items.BOWL))
    // );

    private static Item registerItem(String name, Function<Item.Settings, Item> factory) {
        return registerItem(name, factory, new Item.Settings());
    }

    private static Item registerItem(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Nirvana.MOD_ID, name));
        Item item = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    public static void registerModItems() {
    }
}