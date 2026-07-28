package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.entity.NirvanaEntities;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Real vanilla spawn-egg mechanics (right click to spawn a {@link galena.nirvana.entity.Reefer}),
 * disguised as a creeper spawn egg but carrying its own icon.
 */
public class ReeferSpawnEgg extends SpawnEggItem implements PolymerItem {
    public ReeferSpawnEgg(Settings settings) {
        super(settings.component(DataComponentTypes.ENTITY_DATA, TypedEntityData.create(NirvanaEntities.REEFER, new NbtCompound())));
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.CREEPER_SPAWN_EGG;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return Identifier.of(Nirvana.MOD_ID, "reefer_spawn_egg");
    }
}
