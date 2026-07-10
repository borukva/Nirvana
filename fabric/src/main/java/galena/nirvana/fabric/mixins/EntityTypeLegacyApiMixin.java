package galena.nirvana.fabric.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

// Registrate-fabric 1.3.6-MC1.21.1 calls EntityType.method_5883(Level) (the 1.21.1 signature
// for create). In 1.21.10 that method gained a required EntitySpawnReason parameter, so the
// call site in registrate's JAR cannot be remapped and arrives at runtime as the old descriptor.
// This mixin adds the missing single-arg overload so registrate can find it.
@Mixin(EntityType.class)
public class EntityTypeLegacyApiMixin {
    public Entity method_5883(Level level) {
        return ((EntityType<?>) (Object) this).create(level, EntitySpawnReason.MOB_SUMMONED);
    }
}
