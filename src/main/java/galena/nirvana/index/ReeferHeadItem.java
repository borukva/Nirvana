package galena.nirvana.index;

import eu.pb4.polymer.core.api.item.PolymerItem;
import galena.nirvana.Nirvana;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * Places {@link ReeferHeadBlock} on floors and {@link ReeferWallHeadBlock} on walls, exactly the
 * way vanilla's own mob-head items pick between their two blocks - which is what makes the head
 * mountable on a wall at all.
 */
public class ReeferHeadItem extends StandingAndWallBlockItem implements PolymerItem {
    public ReeferHeadItem(Block standing, Block wall, Properties settings) {
        super(standing, wall, Direction.DOWN, settings);
    }

    /**
     * Deliberately a plain item rather than a vanilla skull: the client's head-render layer
     * special-cases skull block items and draws the stock skull model for them, ignoring the item
     * model entirely - so a skull disguise made a worn reefer head show up as a bare skeleton
     * skull. Any ordinary item gets rendered from {@link #getPolymerItemModel} instead, in hand,
     * in the inventory, on the ground and on the head alike.
     */
    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.PAPER;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context, HolderLookup.Provider registries) {
        return Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "reefer_head_block");
    }

    /**
     * The client believes it is holding a plain item, not a block item, so it never plays a
     * placement sound of its own - the server has to send one, same as
     * {@link NirvanaTexturedBlockItem} does for the other custom blocks.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        if (result != InteractionResult.SUCCESS) {
            return result;
        }

        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            var soundPos = Vec3.atCenterOf(context.getClickedPos().relative(context.getClickedFace()));
            var group = this.getBlock().defaultBlockState().getSoundType();
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(group.getPlaceSound()),
                    SoundSource.BLOCKS,
                    soundPos.x, soundPos.y, soundPos.z,
                    (group.getVolume() + 1.0F) / 2.0F,
                    group.getPitch() * 0.8F,
                    serverPlayer.getRandom().nextLong()
            ));
        }
        return InteractionResult.SUCCESS_SERVER;
    }
}
