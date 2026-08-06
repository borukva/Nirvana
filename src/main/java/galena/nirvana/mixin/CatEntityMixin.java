package galena.nirvana.mixin;

import galena.nirvana.Nirvana;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

/**
 * "Sprigatito"/"Skeker" easter egg: naming a cat exactly either (any case) reskins it, the same
 * way a real name-based reskin (like vanilla's "jeb_" sheep) would if a server-only mod could
 * touch client rendering directly - which it can't, so this swaps the cat's real, synced
 * {@link CatVariant} instead.
 */
@Mixin(Cat.class)
public abstract class CatEntityMixin {
    private static final ResourceKey<CatVariant> SPRIGATITO = ResourceKey.create(Registries.CAT_VARIANT, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "sprigatito"));

    @Unique
    private String nirvana$lastCheckedName = "";
    @Unique
    private Holder<CatVariant> nirvana$variantBeforeSprigatito;

    @Inject(method = "tick", at = @At("HEAD"))
    private void nirvana$checkSprigatito(CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        // Kittens use a scaled-down model, and the reskin texture is only laid out for the adult
        // proportions - applying it to a baby renders warped. Left untouched (not tracked in
        // nirvana$lastCheckedName) so it picks the name up normally the moment it grows up.
        if (self.isBaby()) return;

        Component customName = self.getCustomName();
        String name = customName == null ? "" : customName.getString().trim().toLowerCase(Locale.ROOT);
        if (name.equals(this.nirvana$lastCheckedName)) {
            return;
        }
        this.nirvana$lastCheckedName = name;

        boolean isSprigatito = self.getVariant().is(SPRIGATITO);
        if (name.equals("sprigatito") || name.equals("skeker")) {
            if (!isSprigatito) {
                this.nirvana$variantBeforeSprigatito = self.getVariant();
                nirvana$applySprigatito(self);
            }
        } else if (isSprigatito && this.nirvana$variantBeforeSprigatito != null) {
            ((CatEntityAccessor) self).nirvana$setVariant(this.nirvana$variantBeforeSprigatito);
            this.nirvana$variantBeforeSprigatito = null;
        }
    }

    @Unique
    private static void nirvana$applySprigatito(Cat self) {
        self.level().registryAccess().lookupOrThrow(Registries.CAT_VARIANT)
                .get(SPRIGATITO.identifier())
                .ifPresent(variant -> ((CatEntityAccessor) self).nirvana$setVariant(variant));
    }
}
