package galena.nirvana.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import galena.nirvana.Nirvana;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public class NirvanaEntities {
    public static final EntityType<Reefer> REEFER = register("reefer",
            EntityType.Builder.of(Reefer::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8));

    public static final EntityType<PrimedThc> THC = register("thc",
            EntityType.Builder.<PrimedThc>of(PrimedThc::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .updateInterval(10));

    public static final EntityType<MinecartThc> THC_MINECART = register("thc_minecart",
            EntityType.Builder.<MinecartThc>of(MinecartThc::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8));

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(String path, EntityType.Builder<T> builder) {
        var key = ResourceKey.create(Registries.ENTITY_TYPE, Nirvana.id(path));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(REEFER, Creeper.createAttributes());
        PolymerEntityUtils.registerType(REEFER, THC, THC_MINECART);
    }
}
