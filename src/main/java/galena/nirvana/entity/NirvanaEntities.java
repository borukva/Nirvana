package galena.nirvana.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import galena.nirvana.Nirvana;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class NirvanaEntities {
    public static final EntityType<Reefer> REEFER = register("reefer",
            EntityType.Builder.create(Reefer::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.7F)
                    .maxTrackingRange(8));

    public static final EntityType<PrimedThc> THC = register("thc",
            EntityType.Builder.<PrimedThc>create(PrimedThc::new, SpawnGroup.MISC)
                    .dimensions(0.98F, 0.98F)
                    .makeFireImmune()
                    .maxTrackingRange(10)
                    .trackingTickInterval(10));

    public static final EntityType<MinecartThc> THC_MINECART = register("thc_minecart",
            EntityType.Builder.<MinecartThc>create(MinecartThc::new, SpawnGroup.MISC)
                    .dimensions(0.98F, 0.7F)
                    .maxTrackingRange(8));

    private static <T extends net.minecraft.entity.Entity> EntityType<T> register(String path, EntityType.Builder<T> builder) {
        var key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Nirvana.id(path));
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(REEFER, CreeperEntity.createCreeperAttributes());
        PolymerEntityUtils.registerType(REEFER, THC, THC_MINECART);
    }
}
