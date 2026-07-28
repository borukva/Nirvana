package galena.nirvana.world.gen;

import galena.nirvana.index.NirvanaBlocks;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;

/**
 * Mirrors the original mod's Forge-side global loot modifier: a third of the time, a sniffer's
 * dig turns up a single Bliss Bloom instead of its usual result. Done at drop time (rather than as
 * an extra weighted pool entry added to the table itself) so the roll actually replaces the
 * vanilla result rather than just padding it with a guaranteed extra item.
 */
public class ModLootInjections {
    private static final float BLISS_BLOOM_CHANCE = 0.333F;

    public static void register() {
        LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> {
            if (table.matchesKey(LootTables.SNIFFER_DIGGING_GAMEPLAY)
                    && context.getRandom().nextFloat() < BLISS_BLOOM_CHANCE) {
                drops.clear();
                drops.add(new ItemStack(NirvanaBlocks.BLISS_BLOOM_ITEM));
            }
        });
    }
}
