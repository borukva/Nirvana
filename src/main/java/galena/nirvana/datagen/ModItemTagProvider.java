package galena.nirvana.datagen;

import galena.nirvana.data.ModTags;
import galena.nirvana.index.NirvanaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
     public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(ModTags.SEEDS)
                .add(NirvanaItems.HEMP_SEEDS);

        valueLookupBuilder(ModTags.SMOKING_ITEM)
                .add(NirvanaItems.JOINT)
                .add(NirvanaItems.BONG)
                .add(NirvanaItems.STUFFED_PIPE)
                .add(NirvanaItems.SUSPICIOUS_PIPE);

        valueLookupBuilder(ModTags.NAUSEATING)
                .add(NirvanaItems.JOINT);
    }
}
