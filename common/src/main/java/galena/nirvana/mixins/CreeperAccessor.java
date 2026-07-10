package galena.nirvana.mixins;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperAccessor {

    @Accessor("droppedSkulls")
    boolean getDroppedSkulls();

    @Accessor("droppedSkulls")
    void setDroppedSkulls(boolean droppedSkulls);

}
