package galena.nirvana.datagen;

import galena.nirvana.data.ModTags;
import galena.nirvana.index.NirvanaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
     public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(ModTags.SEEDS)
                .add(key(NirvanaItems.HEMP_SEEDS));

        builder(ModTags.SMOKING_ITEM)
                .add(key(NirvanaItems.JOINT))
                .add(key(NirvanaItems.BONG))
                .add(key(NirvanaItems.STUFFED_PIPE))
                .add(key(NirvanaItems.SUSPICIOUS_PIPE));

        builder(ModTags.NAUSEATING)
                .add(key(NirvanaItems.JOINT));
    }

    // add(...) takes a ResourceKey<Item> now, not the Item itself.
    private static net.minecraft.resources.ResourceKey<Item> key(Item item) {
        return item.builtInRegistryHolder().key();
    }
}
