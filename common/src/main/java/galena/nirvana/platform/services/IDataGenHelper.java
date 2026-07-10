package galena.nirvana.platform.services;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import org.jetbrains.annotations.Nullable;

public interface IDataGenHelper {

    void hempCrop(DataGenContext<Block, ? extends CropBlock> context, RegistrateBlockstateProvider provider);

    void hempCrop(RegistrateBlockLootTables provider, CropBlock block);

    void crate(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider);

    void flatItem(DataGenContext<Item, ? extends Item> context, RegistrateItemModelProvider provider);

    void blissBloom(DataGenContext<Block, ? extends DoublePlantBlock> context, RegistrateBlockstateProvider provider);

    void blissBloom(RegistrateBlockLootTables provider, DoublePlantBlock block);

    void wildHemp(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider);

    void wildHemp(RegistrateBlockLootTables provider, Block block);

    void pipe(DataGenContext<Item, ? extends Item> context, RegistrateItemModelProvider provider);

    void stuffedPipe(DataGenContext<Item, ? extends Item> context, RegistrateRecipeProvider provider);

    void tnt(RegistrateBlockLootTables provider, Block block);

    void tnt(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider);

    void thcMinecart(DataGenContext<Item, ? extends ItemLike> context, RegistrateRecipeProvider provider);

    void thc(DataGenContext<Item, ? extends ItemLike> context, RegistrateRecipeProvider provider);

    void reefer(RegistrateEntityLootTables provider, EntityType<?> type);

    void peaceBannerPattern(DataGenContext<Item, ? extends Item> context, RegistrateRecipeProvider provider);

    void hempBurlap(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider);

    <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> hempBurlap(@Nullable DyeColor color);

    void wovenHempBurlap(DataGenContext<Block, ? extends RotatedPillarBlock> context, RegistrateBlockstateProvider provider);

    <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> wovenHempBurlap(@Nullable DyeColor color);

    void pottedPlant(DataGenContext<Block, ? extends FlowerPotBlock> context, RegistrateBlockstateProvider provider);

    void pottedPlant(RegistrateBlockLootTables provider, Block block);

    void skull(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider);
}
