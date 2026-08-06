package galena.nirvana.index.crop;

import galena.nirvana.Nirvana;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.utils.TransparentPlant;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import eu.pb4.factorytools.api.util.LazyItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import java.util.ArrayList;

public class HempCrop extends CropBlock implements FactoryBlock, TransparentPlant {
    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public HempCrop(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.WHEAT.defaultBlockState();
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return NirvanaItems.HEMP_SEEDS;
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState);
    }

    public static class Model extends BlockModel {
        // Deliberately lazy: resolving real ItemStacks this early (static init, at mod-init time)
        // runs before components are bound and crashes with "Components not bound yet".
        public static final ArrayList<LazyItemStack> MODELS = new ArrayList<>();
        static{
            for (int i = 0; i <= 5; i++){
                MODELS.add(ItemDisplayElementUtil.getModel(Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "block/hemp_crop_stage"+i)));
            }
        }
        public ItemDisplayElement main;
        public Model(BlockState state){
            init(state);
        }
        public void init(BlockState state){
            this.main = ItemDisplayElementUtil.createSimple();
            updateItem(state);
            this.main.setScale(new Vector3f(1));
            this.addElement(main);
        }
        protected void updateItem(BlockState state) {
            LazyItemStack model = switch (state.getValue(AGE)) {
                case 2 -> getModels().get(1);
                case 3, 4 -> getModels().get(2);
                case 5 -> getModels().get(3);
                case 6 -> getModels().get(4);
                case 7 -> getModels().get(5);
                default -> getModels().getFirst();
            };
            this.main.setItem(model.get());
        }
        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE){
                updateItem(this.blockState());
                this.tick();
            }
            super.notifyUpdate(updateType);
        }
        public ArrayList<LazyItemStack> getModels (){
            return MODELS;
        }
    }
}