package galena.nirvana.fabric.mixins;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.EntityBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// In Fabric API for MC 1.21.10, FabricEntityTypeBuilder.build() now requires a
// ResourceKey<EntityType<?>> parameter. Registrate 1.3.6 calls the old no-arg build().
// This mixin redirects the call to pass the correct key.
@Mixin(value = EntityBuilder.class, remap = false)
public abstract class EntityBuilderMixin {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(
        method = "createEntry",
        at = @At(
            value = "INVOKE",
            target = "Lnet/fabricmc/fabric/api/object/builder/v1/entity/FabricEntityTypeBuilder;build()Lnet/minecraft/world/entity/EntityType;"
        )
    )
    private <T extends Entity> EntityType<T> injectEntityId(FabricEntityTypeBuilder<T> builder) {
        AbstractBuilder self = (AbstractBuilder) (Object) this;
        ResourceKey<EntityType<?>> key = ResourceKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(self.getOwner().getModid(), self.getName())
        );
        return builder.build(key);
    }
}
