package galena.nirvana.fabric.mixins;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Registrate 1.3.6-MC1.21.1 creates BlockBehaviour.Properties without calling setId().
// MC 1.21.10 added a requireId() validation in BlockBehaviour.<init> that throws NPE
// when the Properties has no ResourceKey<Block> set. This mixin intercepts createEntry()
// just before the block factory is called and injects the correct key.
@Mixin(value = BlockBuilder.class, remap = false)
public abstract class BlockBuilderMixin {

    @SuppressWarnings("rawtypes")
    @ModifyVariable(
        method = "createEntry",
        at = @At(
            value = "INVOKE",
            target = "Lcom/tterrag/registrate/util/nullness/NonNullFunction;apply(Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 1
        ),
        index = 1
    )
    private BlockBehaviour.Properties injectBlockId(BlockBehaviour.Properties props) {
        // getName() and getOwner() are defined in AbstractBuilder, not BlockBuilder directly.
        // @Shadow can't find inherited methods with remap=false, so we cast instead.
        AbstractBuilder self = (AbstractBuilder) (Object) this;
        ResourceKey<Block> key = ResourceKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(self.getOwner().getModid(), self.getName())
        );
        return props.setId(key);
    }
}
