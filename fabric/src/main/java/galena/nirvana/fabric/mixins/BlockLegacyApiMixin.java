package galena.nirvana.fabric.mixins;

import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

// Registrate-fabric 1.3.6-MC1.21.1 calls Block.method_9539() (the 1.21.1 intermediary name
// for getDescriptionId). In 1.21.10 that method was reassigned a new intermediary ID, so the
// call site in registrate's JAR cannot be remapped and arrives at runtime as "method_9539".
// This mixin adds the missing alias so registrate can find it.
@Mixin(Block.class)
public class BlockLegacyApiMixin {
    public String method_9539() {
        return ((Block) (Object) this).getDescriptionId();
    }
}
