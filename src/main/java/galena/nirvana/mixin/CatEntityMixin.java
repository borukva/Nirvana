package galena.nirvana.mixin;

import galena.nirvana.Nirvana;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

/**
 * "Sprigatito" easter egg: naming a cat exactly that (any case) reskins it. The original mod did
 * this by overriding the client's own texture-selection method, which only worked because that
 * method still hardcoded a texture per variant back then; cat variants are a real data-driven
 * registry now; so here the reskin is a genuine {@link CatVariant} swap - the same kind of change
 * a nametag or a potion effect makes - rather than a render-time trick, and needs no client mod.
 */
@Mixin(CatEntity.class)
public abstract class CatEntityMixin {
    private static final RegistryKey<CatVariant> SPRIGATITO =
            RegistryKey.of(RegistryKeys.CAT_VARIANT, Identifier.of(Nirvana.MOD_ID, "sprigatito"));

    @Unique
    private String nirvana$lastCheckedName = "";
    /** Remembered only in memory - restored on rename-away, but lost across a server restart. */
    @Unique
    private RegistryEntry<CatVariant> nirvana$variantBeforeSprigatito;

    @Inject(method = "tick", at = @At("HEAD"))
    private void nirvana$checkSprigatitoName(CallbackInfo ci) {
        CatEntity self = (CatEntity) (Object) this;
        Text customName = self.getCustomName();
        String name = customName == null ? "" : customName.getString().trim().toLowerCase(Locale.ROOT);
        if (name.equals(this.nirvana$lastCheckedName)) {
            return;
        }
        this.nirvana$lastCheckedName = name;

        boolean isSprigatito = self.getVariant().matchesKey(SPRIGATITO);
        if (name.equals("sprigatito")) {
            if (!isSprigatito) {
                self.getEntityWorld().getRegistryManager().getOrThrow(RegistryKeys.CAT_VARIANT)
                        .getEntry(SPRIGATITO.getValue())
                        .ifPresent(variant -> {
                            this.nirvana$variantBeforeSprigatito = self.getVariant();
                            ((CatEntityAccessor) self).nirvana$setVariant(variant);
                        });
            }
        } else if (isSprigatito && this.nirvana$variantBeforeSprigatito != null) {
            ((CatEntityAccessor) self).nirvana$setVariant(this.nirvana$variantBeforeSprigatito);
            this.nirvana$variantBeforeSprigatito = null;
        }
    }
}
