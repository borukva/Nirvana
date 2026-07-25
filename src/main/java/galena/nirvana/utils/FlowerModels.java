package galena.nirvana.utils;

import galena.nirvana.Nirvana;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class FlowerModels {
    public static final HashMap<String, ItemStack> FLOWER_MODELS = new HashMap<>();
    public static final ItemStack WILD_HEMP = ItemDisplayElementUtil.getModel(Identifier.of(Nirvana.MOD_ID, "block/wild_hemp"));

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void register() {
        FLOWER_MODELS.put("wild_hemp", WILD_HEMP);
        WILD_HEMP.isEmpty();
    }
}