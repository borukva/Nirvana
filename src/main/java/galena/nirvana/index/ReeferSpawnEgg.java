package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.entity.NirvanaEntities;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * Real vanilla spawn-egg mechanics (right click to spawn a {@link galena.nirvana.entity.Reefer}),
 * disguised as a creeper spawn egg but carrying its own icon.
 */
public class ReeferSpawnEgg extends SpawnEggItem implements PolymerItem {
    public ReeferSpawnEgg(Properties settings) {
        super(settings.component(DataComponents.ENTITY_DATA, TypedEntityData.of(NirvanaEntities.REEFER, new CompoundTag())));
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.CREEPER_SPAWN_EGG;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context, HolderLookup.Provider registries) {
        return Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "reefer_spawn_egg");
    }
}
