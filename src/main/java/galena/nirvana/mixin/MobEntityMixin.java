package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @WrapWithCondition(
            method = "tickNewAi",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/entity/ai/goal/GoalSelector.tickGoals (Z)V",
                    ordinal = 0
            )
    )
    public boolean interruptTargetGoal(GoalSelector targetSelector, boolean argument) {
        @SuppressWarnings("DataFlowIssue")
        var self = (MobEntity) (Object) (this);
        return !self.hasStatusEffect(NirvanaEffects.PEACE);
    }

    @WrapWithCondition(
            method = "tickNewAi",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/entity/ai/goal/GoalSelector.tick ()V",
                    ordinal = 0
            )
    )
    public boolean interruptTargetGoal(GoalSelector targetSelector) {
        @SuppressWarnings("DataFlowIssue")
        var self = (MobEntity) (Object) (this);
        return !self.hasStatusEffect(NirvanaEffects.PEACE);
    }

    @ModifyExpressionValue(
            method = "interactWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/item/ItemStack.isOf (Lnet/minecraft/item/Item;)Z",
                    ordinal = 0
            )
    )
    public boolean checkAndHandleImportantInteractions(boolean original, @Local ItemStack stack) {
        return original || stack.isOf(NirvanaItems.HERBAL_SALVE);
    }

}
