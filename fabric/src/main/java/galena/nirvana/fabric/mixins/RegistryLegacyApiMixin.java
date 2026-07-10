package galena.nirvana.fabric.mixins;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;

// Registrate-fabric 1.3.6-MC1.21.1 calls Registry.method_30517() (the 1.21.1 intermediary
// name for key()). In 1.21.10 that method was reassigned a new intermediary ID.
// This mixin adds the missing alias as a default interface method.
@Mixin(Registry.class)
public interface RegistryLegacyApiMixin {
    @SuppressWarnings("unchecked")
    default ResourceKey<? extends Registry<?>> method_30517() {
        return (ResourceKey<? extends Registry<?>>) ((Registry<?>) this).key();
    }
}
