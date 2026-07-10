package galena.nirvana.fabric.world.item;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public class FabricSpawnEggItem extends SpawnEggItem {

    private final NonNullSupplier<? extends EntityType<? extends Mob>> type;

    public FabricSpawnEggItem(NonNullSupplier<? extends EntityType<? extends Mob>> type, int primary, int secondary, Properties properties) {
        super(properties);
        this.type = type;
        // Note: entity type and colors are set via DataComponents on Properties by the caller
    }

    @Override
    public EntityType<?> getType(ItemStack stack) {
        return type.get();
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return type.get().requiredFeatures();
    }
}
