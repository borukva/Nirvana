package galena.nirvana.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperEntityAccessor {
    @Accessor("droppedSkulls")
    boolean getHeadsDropped();

    @Accessor("droppedSkulls")
    void setHeadsDropped(boolean headsDropped);
}
