package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.function.Consumer;

/**
 * Unlike the rest of the {@link SmokingItem} family, this is disguised as a genuine
 * {@code Items.POTION} rather than {@code Items.BOW} - vanilla's brewing stand slot-insertion
 * check is hardcoded to a handful of real vanilla items, and only a stack whose real network
 * identity matches one of them is accepted by an unmodified client, so this can actually be
 * dragged into a stand's potion slot by hand. The disguise item and the use-animation are
 * independent choices as far as Polymer is concerned, so the bow-draw "smoking" animation
 * ({@link UseAction#BOW}) is kept; this also no longer needs the BowItem-arrow-firing workaround
 * the rest of the family has to guard against, since it doesn't extend {@code BowItem} at all.
 */
public class BongItem extends Item implements PolymerItem {
    private static final int COOLDOWN_TICKS = 20;
    private static final int USE_DURATION = 40;

    private final List<StatusEffectInstance> effects;
    private final ItemStack remainder;

    public BongItem(Settings settings, List<StatusEffectInstance> effects, ItemStack remainder) {
        super(settings);
        this.effects = effects;
        this.remainder = remainder;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item." + Nirvana.MOD_ID + ".bong");
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.POTION;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return Identifier.of(Nirvana.MOD_ID, "bong");
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(stack)) {
            return ActionResult.PASS;
        }

        user.setCurrentHand(hand);
        return ActionResult.SUCCESS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world instanceof ServerWorld serverWorld) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    NirvanaSounds.BONG, user.getSoundCategory(), 0.5F, 1.0F);

            SmokingItem.scheduleSmoke(serverWorld, user.getUuid());
            for (StatusEffectInstance effect : effects) {
                SmokingItem.applyEffect(user, effect);
            }

            boolean creative = false;
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(stack, COOLDOWN_TICKS);
                player.incrementStat(Stats.USED.getOrCreateStat(this));
                creative = player.getAbilities().creativeMode;
            }

            // Creative mode never spends the item, same as vanilla consumables.
            if (!creative) {
                if (stack.isDamageable()) {
                    stack.setDamage(stack.getDamage() + 1);
                    if (stack.getDamage() >= stack.getMaxDamage()) {
                        return this.remainder.copy();
                    }
                } else {
                    return this.remainder.copy();
                }
            }
        }

        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack,
                              TooltipContext context,
                              TooltipDisplayComponent displayComponent,
                              Consumer<Text> tooltip,
                              TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, tooltip, type);
        SmokingItem.appendEffectTooltip(effects, tooltip);
    }
}
