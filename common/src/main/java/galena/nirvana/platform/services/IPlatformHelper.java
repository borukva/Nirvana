package galena.nirvana.platform.services;

import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import com.tterrag.registrate.builders.EntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import galena.nirvana.platform.registrate.EntityPropertiesBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;

public interface IPlatformHelper {

    MultikultiRegistrate<?> getRegistrate();

    default Item createSpawnEggItem(NonNullSupplier<? extends EntityType<? extends Mob>> type, int primary, int secodary, Item.Properties properties) {
        // SpawnEggItem constructor changed in MC 1.21.4+ - entity type and colors set via DataComponents
        // Override this in platform-specific implementation
        return new SpawnEggItem(properties);
    }

    <E extends Entity, P> NonNullFunction<EntityBuilder<E, P>, EntityBuilder<E, P>> entityProperties(NonNullConsumer<EntityPropertiesBuilder> factory);

    Ingredient createNBTIngredient(ItemStack stack);

    boolean isFakePlayer(LivingEntity entity);

    boolean createLoaded();

}
