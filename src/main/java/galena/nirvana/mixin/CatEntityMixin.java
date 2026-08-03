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
 * "Sprigatito"/"Skeker" easter egg: naming a cat exactly either (any case) reskins it, the same
 * way a real name-based reskin (like vanilla's "jeb_" sheep) would if a server-only mod could
 * touch client rendering directly - which it can't, so this swaps the cat's real, synced
 * {@link CatVariant} instead.
 */
@Mixin(CatEntity.class)
public abstract class CatEntityMixin {
    private static final RegistryKey<CatVariant> SPRIGATITO = RegistryKey.of(RegistryKeys.CAT_VARIANT, Identifier.of(Nirvana.MOD_ID, "sprigatito"));

    @Unique
    private String nirvana$lastCheckedName = "";
    @Unique
    private RegistryEntry<CatVariant> nirvana$variantBeforeSprigatito;

    @Inject(method = "tick", at = @At("HEAD"))
    private void nirvana$checkSprigatito(CallbackInfo ci) {
        CatEntity self = (CatEntity) (Object) this;

        Text customName = self.getCustomName();
        String name = customName == null ? "" : customName.getString().trim().toLowerCase(Locale.ROOT);
        if (name.equals(this.nirvana$lastCheckedName)) {
            return;
        }
        this.nirvana$lastCheckedName = name;

        boolean isSprigatito = self.getVariant().matchesKey(SPRIGATITO);
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
    private static void nirvana$applySprigatito(CatEntity self) {
        self.getEntityWorld().getRegistryManager().getOrThrow(RegistryKeys.CAT_VARIANT)
                .getEntry(SPRIGATITO.getValue())
                .ifPresent(variant -> ((CatEntityAccessor) self).nirvana$setVariant(variant));
    }
}
