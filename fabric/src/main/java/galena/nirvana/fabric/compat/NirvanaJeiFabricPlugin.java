package galena.nirvana.fabric.compat;

import galena.nirvana.compat.NirvanaJeiCompat;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.index.NirvanaRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;

public class NirvanaJeiFabricPlugin implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.CRAFTING, NirvanaRecipeTypes.createSuspiciousRecipes());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return NirvanaJeiCompat.ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(NirvanaItems.POTION_BONG.get(), DataComponents.POTION_CONTENTS);
        registration.registerFromDataComponentTypes(NirvanaItems.HERBAL_SALVE.get(), DataComponents.SUSPICIOUS_STEW_EFFECTS);
        registration.registerFromDataComponentTypes(NirvanaItems.SUSPICIOUS_PIPE.get(), DataComponents.SUSPICIOUS_STEW_EFFECTS);
    }

}
