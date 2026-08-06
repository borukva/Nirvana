package galena.nirvana.index;

import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import galena.nirvana.Nirvana;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * BlissBloom's block disguise is an invisible placeholder (its real look comes from an
 * item-display virtual entity), so {@link PolymerBlockItem}'s default item-icon derivation - which
 * mirrors the block's own texture request - has nothing to draw from. This overrides the item
 * model explicitly instead of relying on that derivation.
 */
public class BlissBloomItem extends PolymerBlockItem {

    public BlissBloomItem(Block block, Properties settings) {
        super(block, settings, Items.POPPY);
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider registries) {
        return Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "bliss_bloom");
    }
}
