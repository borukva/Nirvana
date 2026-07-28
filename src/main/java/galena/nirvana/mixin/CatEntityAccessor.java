package galena.nirvana.mixin;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CatEntity.class)
public interface CatEntityAccessor {
    @Invoker("setVariant")
    void nirvana$setVariant(RegistryEntry<CatVariant> variant);
}
