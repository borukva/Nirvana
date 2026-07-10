package galena.nirvana.index;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;
import galena.nirvana.platform.Services;
import galena.nirvana.world.entity.MinecartThc;
import galena.nirvana.world.entity.PrimedThc;
import galena.nirvana.world.entity.Reefer;
import galena.nirvana.world.entity.renderer.CustomTntRenderer;
import galena.nirvana.world.entity.renderer.ReeferRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.MinecartTNT;

public class NirvanaEntities {

    private static final AbstractRegistrate<?> REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final EntityEntry<? extends Mob> REEFER = REGISTRATE
            .entity("reefer", Reefer::new, MobCategory.MONSTER)
            .attributes(Reefer::createAttributes)
            .transform(Services.PLATFORM.entityProperties(builder -> builder
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8)
            ))
            .loot(Services.DATAGEN::reefer)
            .renderer(() -> ctx -> (net.minecraft.client.renderer.entity.EntityRenderer) new ReeferRenderer(ctx))
            .register();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final EntityEntry<PrimedThc> THC = (EntityEntry<PrimedThc>) (EntityEntry) REGISTRATE
            .<PrimedThc>entity("thc", PrimedThc::new, MobCategory.MISC)
            .lang("THC")
            .renderer(() -> ctx -> (net.minecraft.client.renderer.entity.EntityRenderer) CustomTntRenderer.of(NirvanaBlocks.THC).apply(ctx))
            .transform(Services.PLATFORM.entityProperties(builder -> builder
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
            ))
            .register();

    public static final EntityEntry<? extends MinecartTNT> THC_MINECART = REGISTRATE
            .<MinecartThc>entity("thc_minecart", MinecartThc::new, MobCategory.MISC)
            .lang("Minecart with THC")
            .transform(Services.PLATFORM.entityProperties(builder -> builder
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
            ))
            .renderer(() -> ctx -> (net.minecraft.client.renderer.entity.EntityRenderer) new TntMinecartRenderer(ctx))
            .register();

    public static void register() {
        // loads this class
    }

}
