package galena.nirvana.utils;

import galena.nirvana.Nirvana;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.factorytools.api.util.LazyItemStack;
import net.minecraft.resources.Identifier;

import java.util.HashMap;

public class FlowerModels {
    public static final HashMap<String, LazyItemStack> FLOWER_MODELS = new HashMap<>();
    // Deliberately lazy: resolving a real ItemStack this early (static init, at mod-init time)
    // runs before components are bound and crashes with "Components not bound yet".
    public static final LazyItemStack WILD_HEMP = ItemDisplayElementUtil.getModel(Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "block/wild_hemp"));

    public static void register() {
        FLOWER_MODELS.put("wild_hemp", WILD_HEMP);
    }
}