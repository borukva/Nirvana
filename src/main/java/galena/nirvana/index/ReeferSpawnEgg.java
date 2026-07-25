package galena.nirvana.index;

import galena.nirvana.entity.NirvanaEntities;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Real vanilla spawn-egg mechanics (right click to spawn a {@link galena.nirvana.entity.Reefer}),
 * disguised as a plain creeper spawn egg — no custom baked texture, since a vanilla client only
 * ever sees the disguise target's own icon regardless of what we set here.
 */
public class ReeferSpawnEgg extends SpawnEggItem implements PolymerItem {
    public ReeferSpawnEgg(Settings settings) {
        super(settings.component(DataComponentTypes.ENTITY_DATA, TypedEntityData.create(NirvanaEntities.REEFER, new NbtCompound())));
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.CREEPER_SPAWN_EGG;
    }
}
