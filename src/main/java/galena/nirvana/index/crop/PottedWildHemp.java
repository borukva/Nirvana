package galena.nirvana.index.crop;

import galena.nirvana.Nirvana;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FlowerPotBlock;
import eu.pb4.factorytools.api.util.LazyItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * A standalone flowerpot holding {@link galena.nirvana.index.NirvanaBlocks#WILD_HEMP}.
 * Vanilla {@code FlowerPotBlock}'s constructor registers {@code content -> this} into its own
 * private {@code CONTENT_TO_POTTED} map as a side effect, and every empty flower pot's
 * item-use handler reads that same shared map - so simply constructing this block (once, at
 * class init, same as every vanilla potted-plant variant) is what makes right-clicking a
 * vanilla empty pot with wild hemp place this block; no mixin needed.
 * <p>
 * The actual disguise is invisible (same {@code PLANT_BLOCK} placeholder trick as
 * {@link WildHemp}) - a real potted-fern disguise would render vanilla's own fern on top of
 * the pot instead of hemp, so the visible pot+plant is instead a single custom model
 * ({@code block/potted_wild_hemp}, reusing vanilla's own {@code block/flower_pot_cross}
 * template with the same {@code wild_hemp} texture the standing plant uses) shown via an
 * {@link ItemDisplayElement}.
 */
public class PottedWildHemp extends FlowerPotBlock implements PolymerTexturedBlock, FactoryBlock {
    // Deliberately lazy: resolving a real ItemStack this early (static init, at mod-init time)
    // runs before components are bound and crashes with "Components not bound yet".
    private static final LazyItemStack DISPLAY_MODEL = ItemDisplayElementUtil.getModel(Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "block/potted_wild_hemp"));
    private final BlockState model;

    public PottedWildHemp(Block content, Properties settings) {
        super(content, settings);
        this.model = PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return new Model();
    }

    static class Model extends BlockModel {
        public ItemDisplayElement main;

        public Model() {
            this.main = ItemDisplayElementUtil.createSimple(DISPLAY_MODEL);
            addElement(main);
        }
    }
}
