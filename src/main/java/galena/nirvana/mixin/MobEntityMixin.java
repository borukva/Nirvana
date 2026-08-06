package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public class MobEntityMixin {
    // customServerAiStep is now an empty overridable hook - the actual goal-selector pump (calls
    // to both targetSelector and goalSelector's tick()/tickRunningGoals(boolean), targetSelector
    // first) moved into Mob's own final serverAiStep(), so these wrap that instead. tickGoals(Z)
    // was also renamed to tickRunningGoals(Z).
    @WrapWithCondition(
            method = "serverAiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;tickRunningGoals(Z)V",
                    ordinal = 0
            )
    )
    public boolean interruptTargetGoal(GoalSelector targetSelector, boolean argument) {
        @SuppressWarnings("DataFlowIssue")
        var self = (Mob) (Object) (this);
        return !self.hasEffect(NirvanaEffects.PEACE);
    }

    @WrapWithCondition(
            method = "serverAiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;tick()V",
                    ordinal = 0
            )
    )
    public boolean interruptTargetGoal(GoalSelector targetSelector) {
        @SuppressWarnings("DataFlowIssue")
        var self = (Mob) (Object) (this);
        return !self.hasEffect(NirvanaEffects.PEACE);
    }

    // interactWithItem is called checkAndHandleImportantInteractions now (coincidentally the
    // same name this handler method already had), and its own isOf(Item) check became
    // is(Object) (isOf doesn't exist anymore - see the .getItem() == comparisons used elsewhere
    // in this codebase for the same rename).
    @ModifyExpressionValue(
            method = "checkAndHandleImportantInteractions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
                    ordinal = 0
            )
    )
    public boolean checkAndHandleImportantInteractions(boolean original, @Local ItemStack stack) {
        return original || stack.getItem() == (NirvanaItems.HERBAL_SALVE);
    }

}
