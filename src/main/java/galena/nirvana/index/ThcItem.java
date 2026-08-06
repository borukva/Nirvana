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
 * THC's block disguise uses a multi-texture (top/side/bottom) model, and PolymerBlockItem's
 * default item-icon derivation from the block's own texture request doesn't reliably carry that
 * over to the inventory icon (unlike single-texture blocks like reefer_head) - so the item model
 * is set explicitly instead of relying on that derivation.
 */
public class ThcItem extends PolymerBlockItem {

    public ThcItem(Block block, Properties settings) {
        super(block, settings, Items.TNT);
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider registries) {
        return Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "thc");
    }
}
