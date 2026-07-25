package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * A bong variant pre-filled with a vanilla potion's effects (dynamic per-stack, via the stack's
 * own {@code POTION_CONTENTS} component), rather than a fixed effect list baked into the item
 * like {@link SmokingItem}. One stack per registered potion shows up in the creative tab.
 */
public class PotionBongItem extends BowItem implements PolymerItem {
    private static final int COOLDOWN_TICKS = 20;
    private static final int USE_DURATION = 40;

    public PotionBongItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        var contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        var base = Text.translatable("item." + Nirvana.MOD_ID + ".potion_bong");
        return contents != null ? contents.getName("item." + Nirvana.MOD_ID + ".potion_bong.effect.") : base;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.BOW;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return Identifier.of(Nirvana.MOD_ID, "potion_bong");
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
    }

    /**
     * Without this, BowItem's own vanilla implementation fires an arrow if the use is released
     * before getMaxUseTime is reached.
     */
    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        return false;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.get(DataComponentTypes.POTION_CONTENTS) == null) {
            return ActionResult.PASS;
        }
        if (user.getItemCooldownManager().isCoolingDown(stack)) {
            return ActionResult.PASS;
        }
        user.setCurrentHand(hand);
        return ActionResult.SUCCESS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        var contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (world instanceof ServerWorld && contents != null) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    NirvanaSounds.BONG, user.getSoundCategory(), 0.5F, 1.0F);

            SmokingItem.scheduleSmoke((ServerWorld) world, user.getUuid());
            contents.apply(user, 1.0F);

            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(stack, COOLDOWN_TICKS);
                player.incrementStat(Stats.USED.getOrCreateStat(this));
            }

            if (stack.isDamageable()) {
                stack.setDamage(stack.getDamage() + 1);
                if (stack.getDamage() >= stack.getMaxDamage()) {
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
            } else {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
        }

        // Deliberately not calling super.finishUsing(...): BowItem's own implementation looks
        // for an arrow to fire, which has nothing to do with this item.
        return stack;
    }
}
