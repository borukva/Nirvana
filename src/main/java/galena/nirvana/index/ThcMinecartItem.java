package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.entity.NirvanaEntities;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class ThcMinecartItem extends MinecartItem implements PolymerItem {
    public ThcMinecartItem(Settings settings) {
        super(NirvanaEntities.THC_MINECART, settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.TNT_MINECART;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return Identifier.of(Nirvana.MOD_ID, "thc_minecart");
    }
}
