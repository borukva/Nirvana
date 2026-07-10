package galena.nirvana.world.item;

import galena.nirvana.NirvanaConstants;
import galena.nirvana.index.NirvanaSounds;
import galena.nirvana.platform.Services;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PotionBongItem extends SmokingItem {

    public static final String PATTERN_TRANSLATION_KEY = NirvanaConstants.MOD_ID + ".pattern.potion";

    private static MobEffectInstance modify(MobEffectInstance instance) {
        return new MobEffectInstance(
                instance.getEffect(),
                instance.getDuration() / Services.CONFIG.common().getBongHits(),
                instance.getAmplifier(),
                instance.isAmbient(),
                instance.isVisible(),
                instance.showIcon(),
                null
        );
    }

    public PotionBongItem(Properties properties) {
        super(properties);
    }

    @Override
    Stream<MobEffectInstance> getEffects(ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity) {
        var contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) return Stream.empty();
        return StreamSupport.stream(contents.getAllEffects().spliterator(), false).map(PotionBongItem::modify);
    }

    @Override
    double getRadius(ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity) {
        return Services.CONFIG.common().bongRadius();
    }

    @Override
    public Component getName(ItemStack stack) {
        if (!Services.CONFIG.common().generateBongTranslations()) {
            return super.getName(stack);
        }

        var language = Language.getInstance();
        if (language.has(PATTERN_TRANSLATION_KEY)) try {
            var contents = stack.get(DataComponents.POTION_CONTENTS);
            if (contents != null && contents.potion().isPresent()) {
                var pattern = Pattern.compile(language.getOrDefault(PATTERN_TRANSLATION_KEY));
                var potionName = contents.getName(getDescriptionId() + ".effect.");
                var potionTranslation = language.getOrDefault(potionName.getString());
                var matcher = pattern.matcher(potionTranslation);
                if (matcher.find()) {
                    var translation = matcher.group(1);
                    return Component.translatable(getDescriptionId(), translation);
                }
            }
        } catch (PatternSyntaxException | IllegalStateException | IndexOutOfBoundsException ex) {
            NirvanaConstants.LOGGER.debug("Unable to translation potion bong automatically", ex);
        }

        // In MC 1.21.10, Item.getName(ItemStack) reads DataComponents.ITEM_NAME instead of
        // delegating to getDescriptionId(ItemStack), so we must construct the name manually.
        var contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return Component.translatable(contents.getName(getDescriptionId() + ".effect.").getString());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        // PotionContents implements TooltipProvider in 1.21.10 and adds its tooltip automatically.
        // No manual call needed here — that caused effects to appear twice.
    }

    @Override
    protected @Nullable SoundEvent getUseSound() {
        return NirvanaSounds.BONG.get();
    }

}
