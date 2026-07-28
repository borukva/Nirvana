package galena.nirvana.index;

import eu.pb4.polymer.core.api.item.PolymerItem;
import galena.nirvana.Nirvana;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Places {@link ReeferHeadBlock} on floors and {@link ReeferWallHeadBlock} on walls, exactly the
 * way vanilla's own mob-head items pick between their two blocks - which is what makes the head
 * mountable on a wall at all.
 */
public class ReeferHeadItem extends VerticallyAttachableBlockItem implements PolymerItem {
    public ReeferHeadItem(Block standing, Block wall, Settings settings) {
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
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return Identifier.of(Nirvana.MOD_ID, "reefer_head_block");
    }

    /**
     * The client believes it is holding a plain item, not a block item, so it never plays a
     * placement sound of its own - the server has to send one, same as
     * {@link NirvanaTexturedBlockItem} does for the other custom blocks.
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult result = super.useOnBlock(context);
        if (result != ActionResult.SUCCESS) {
            return result;
        }

        if (context.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
            var soundPos = Vec3d.ofCenter(context.getBlockPos().offset(context.getSide()));
            var group = this.getBlock().getDefaultState().getSoundGroup();
            serverPlayer.networkHandler.sendPacket(new PlaySoundS2CPacket(
                    Registries.SOUND_EVENT.getEntry(group.getPlaceSound()),
                    SoundCategory.BLOCKS,
                    soundPos.x, soundPos.y, soundPos.z,
                    (group.getVolume() + 1.0F) / 2.0F,
                    group.getPitch() * 0.8F,
                    serverPlayer.getRandom().nextLong()
            ));
        }
        return ActionResult.SUCCESS_SERVER;
    }
}
