package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.utils.ModFoodComponents;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Rarity;

import java.util.List;
import java.util.function.Function;
public class NirvanaItems {
    /**
     * bong/potion_bong are disguised as Items.POTION (so they can be dragged into a brewing
     * stand's slot by hand) but a real vanilla PotionItem doesn't override getUseAnimation()/
     * getUseDuration() in Java - it falls back to reading this component straight off the stack,
     * which IS preserved across the Polymer disguise. Setting it here is what actually restores
     * the bow-draw "smoking" animation client-side, not the Java getUseAnimation() override (which
     * only matters server-side, since the client only ever runs the disguise item's own class).
     * <p>
     * {@code ItemStack.usageTick()} spawns this component's particles/sound periodically purely
     * because the component is present, regardless of its useAction value - unrelated to (and
     * would otherwise sound/look like eating alongside) our own playSound/particle logic in
     * {@link BongItem}/{@link PotionBongItem}, hence silenced below.
     */
    private static final Consumable BOW_USE_ANIMATION = Consumable.builder()
            .consumeSeconds(2.0F)
            .animation(ItemUseAnimation.BOW)
            .sound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(net.minecraft.sounds.SoundEvents.EMPTY))
            .hasConsumeParticles(false)
            .build();

    public static final Item HEMP = registerItem("hemp", SimplePolymerItem::new, new Item.Properties());
    public static final Item WEED = registerItem("weed", SimplePolymerItem::new, new Item.Properties());
    public static Item HEMP_SEEDS = registerItem("hemp_seeds",  settings -> new PolymerBlockItem(NirvanaBlocks.HEMP, settings));
    public static Item WILD_HEMP = registerItem("wild_hemp",  settings -> new PolymerBlockItem(NirvanaBlocks.WILD_HEMP, settings));
    public static final Item WEED_BROWNIE  = registerItem("weed_brownie", SimplePolymerItem::new, new Item.Properties().food(new FoodProperties(2, 0.1f, false), ModConsumableComponents.BROWNIE));
    public static final Item HEMP_CLOTH = registerItem("hemp_cloth", SimplePolymerItem::new, new Item.Properties());

    public static final Item POTION_BONG = registerItem("potion_bong",
            settings -> new PotionBongItem(settings.durability(4).component(DataComponents.CONSUMABLE, BOW_USE_ANIMATION)));

    public static final Item PEACE_BANNER_PATTERN = registerItem("peace_banner_pattern", SimplePolymerItem::new,
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                    .delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, registries -> registries.lookupOrThrow(Registries.BANNER_PATTERN)
                            .getOrThrow(TagKey.create(Registries.BANNER_PATTERN, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "peace_banner_patterns")))));

    public static final Item JOINT = registerItem(
            "joint",
            settings -> new SmokingItem(settings.durability(3), List.of(
                    new MobEffectInstance(NirvanaEffects.PEACE, 400, 0)
            ), "joint", Items.AIR, NirvanaSounds.SMOKING)
    );

    public static final Item BONG = registerItem(
            "bong",
            settings -> new BongItem(settings.durability(6).component(DataComponents.CONSUMABLE, BOW_USE_ANIMATION), List.of(
                    new MobEffectInstance(NirvanaEffects.PEACE, 600, 0)
            ), Items.GLASS_BOTTLE)
    );

    public static final Item MUSIC_DISC_JAM = registerItem("music_disc_jam", SimplePolymerItem::new,
            new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
                    // A standalone (unbound) Holder has no real value to serialize - encoding it for
                    // network sync throws "Trying to access unbound value" the moment a player joins.
                    // Has to be resolved against the real data/nirvana/jukebox_song/jam.json entry
                    // once registries are actually loaded, same as PROVIDES_BANNER_PATTERNS above.
                    .delayedComponent(DataComponents.JUKEBOX_PLAYABLE, registries -> new JukeboxPlayable(
                            registries.lookupOrThrow(Registries.JUKEBOX_SONG)
                                    .getOrThrow(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "jam"))))));

    public static final Item REEFER_SPAWN_EGG = registerItem("reefer_spawn_egg", ReeferSpawnEgg::new, new Item.Properties());
    public static final Item THC_MINECART = registerItem("thc_minecart", ThcMinecartItem::new, new Item.Properties().stacksTo(1));

    public static final Item OLD_PIPE = registerItem("old_pipe", SimplePolymerItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final Item STUFFED_PIPE = registerItem(
            "stuffed_pipe",
            settings -> new SmokingItem(settings.durability(12).rarity(Rarity.UNCOMMON), List.of(
                    new MobEffectInstance(NirvanaEffects.PEACE, 600, 0)
            ), "stuffed_pipe", NirvanaItems.OLD_PIPE, NirvanaSounds.SMOKING, true)
    );

    public static final Item SUSPICIOUS_PIPE = registerItem(
            "suspicious_pipe",
            settings -> new SuspiciousPipeItem(settings.durability(12).rarity(Rarity.UNCOMMON))
    );

    public static final Item HERBAL_SALVE = registerItem(
            "herbal_salve",
            settings -> new HerbalSalveItem(settings.stacksTo(1), Items.BOWL)
    );

    // Archived per request - a salve that gets someone else high on contact. May come back later.
    // public static final Item PEACE_SALVE = registerItem(
    //         "peace_salve",
    //         settings -> new SalveItem(settings.stacksTo(1), List.of(
    //                 new MobEffectInstance(NirvanaEffects.PEACE, 600, 0)
    //         ), "peace_salve", new ItemStack(Items.BOWL))
    // );

    private static Item registerItem(String name, Function<Item.Properties, Item> factory) {
        return registerItem(name, factory, new Item.Properties());
    }

    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        var key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, name));
        Item item = factory.apply(settings.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void registerModItems() {
        // Archived per request - dispensers "smoking" a loaded joint/pipe onto whatever's in
        // front of them. Only the joint applies its effect through a dispenser in the original
        // mod; the pipes register the same behaviour purely for their smoke particles.
        // Bong/potion_bong/old_pipe never registered one there, so they keep vanilla's plain
        // "eject the item" dispense regardless. May come back later.
        // SmokingItem.registerDispenserBehavior((SmokingItem) JOINT, true);
        // SmokingItem.registerDispenserBehavior((SmokingItem) STUFFED_PIPE, false);
        // SmokingItem.registerDispenserBehavior((SmokingItem) SUSPICIOUS_PIPE, false);
    }
}
