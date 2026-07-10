package galena.nirvana.fabric.mixins;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Same fix as BlockBuilderMixin: MC 1.21.10 requires Item.Properties.setId() before
// Item.<init> is called, but registrate 1.3.6 never sets it.
@Mixin(value = ItemBuilder.class, remap = false)
public abstract class ItemBuilderMixin {

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
    private Item.Properties injectItemId(Item.Properties props) {
        AbstractBuilder self = (AbstractBuilder) (Object) this;
        ResourceKey<Item> key = ResourceKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(self.getOwner().getModid(), self.getName())
        );
        return props.setId(key);
    }
}
