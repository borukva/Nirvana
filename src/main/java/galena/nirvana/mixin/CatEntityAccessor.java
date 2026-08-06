package galena.nirvana.mixin;

import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Cat.class)
public interface CatEntityAccessor {
    @Invoker("setVariant")
    void nirvana$setVariant(Holder<CatVariant> variant);
}
