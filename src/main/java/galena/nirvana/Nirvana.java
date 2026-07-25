package galena.nirvana;

import galena.nirvana.advancement.NirvanaAdvancements;
import galena.nirvana.data.ModTags;
import galena.nirvana.data.NirvanaBrewingRecipes;
import galena.nirvana.data.NirvanaRecipeTypes;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.entity.NirvanaEntities;
import galena.nirvana.utils.FlowerModels;
import galena.nirvana.index.crop.HempCrop;
import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.world.gen.ModFlowerGeneration;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nirvana implements ModInitializer {
	public static final String MOD_ID = "nirvana";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        NirvanaBlocks.registerBlocks();
        NirvanaItems.registerModItems();
        NirvanaEntities.register();
        FlowerModels.register();
        ModFlowerGeneration.generateFlowers();
        NirvanaEffects.register();
        ModTags.register();
        NirvanaRecipeTypes.register();
        NirvanaBrewingRecipes.register();
        NirvanaAdvancements.register();
        initModels();
        if (PolymerResourcePackUtils.addModAssets(MOD_ID)) {
            ResourcePackExtras.forDefault().addBridgedModelsFolder(id("block"), id("item"));
            LOGGER.info("Successfully added mod assets for " + MOD_ID);
        } else {
            LOGGER.error("Failed to add mod assets for " + MOD_ID);
        }
        PolymerResourcePackUtils.markAsRequired();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void initModels(){
        HempCrop.Model.MODELS.forEach(ItemStack::isEmpty);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}