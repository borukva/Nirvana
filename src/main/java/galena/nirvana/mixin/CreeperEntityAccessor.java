package galena.nirvana.mixin;

import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreeperEntity.class)
public interface CreeperEntityAccessor {
    @Accessor("headsDropped")
    boolean getHeadsDropped();

    @Accessor("headsDropped")
    void setHeadsDropped(boolean headsDropped);
}
