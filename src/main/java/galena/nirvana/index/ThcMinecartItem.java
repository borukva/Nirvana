package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.entity.NirvanaEntities;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.resources.Identifier;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

public class ThcMinecartItem extends MinecartItem implements PolymerItem {
    public ThcMinecartItem(Properties settings) {
        super(NirvanaEntities.THC_MINECART, settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.TNT_MINECART;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context, HolderLookup.Provider registries) {
        return Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "thc_minecart");
    }
}
