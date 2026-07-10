package galena.nirvana.fabric.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.world.block.ICustomTntBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TntBlock.class)
public class TntBlockMixin {

    @Unique
    private boolean shouldPrime(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (this instanceof ICustomTntBlock tnt) {
            tnt.onCaughtFire(state, level, pos, face, igniter);
            return false;
        }
        return true;
    }

    @WrapOperation(
            method = "onPlace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    public boolean onPlace(Level level, BlockPos pos, Operation<Boolean> original,
            @Local(ordinal = 0, argsOnly = true) BlockState state) {
        if (!shouldPrime(state, level, pos, null, null)) return false;
        return original.call(level, pos);
    }

    @WrapOperation(
            method = "neighborChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    public boolean neighborChanged(Level level, BlockPos pos, Operation<Boolean> original,
            @Local(ordinal = 0, argsOnly = true) BlockState state) {
        if (!shouldPrime(state, level, pos, null, null)) return false;
        return original.call(level, pos);
    }

    @WrapOperation(
            method = "playerWillDestroy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    public boolean playerWillDestroy(Level level, BlockPos pos, Operation<Boolean> original,
            @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Player player) {
        if (!shouldPrime(state, level, pos, null, player)) return false;
        return original.call(level, pos);
    }

    @WrapOperation(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z"
            )
    )
    public boolean useItemOn(Level level, BlockPos pos, LivingEntity igniter, Operation<Boolean> original,
            @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) BlockHitResult hit) {
        if (!shouldPrime(state, level, pos, hit.getDirection(), igniter)) return false;
        return original.call(level, pos, igniter);
    }

    @WrapOperation(
            method = "onProjectileHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z"
            )
    )
    public boolean onProjectileHit(Level level, BlockPos pos, LivingEntity igniter, Operation<Boolean> original,
            @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) BlockHitResult hit) {
        if (!shouldPrime(state, level, pos, hit.getDirection(), igniter)) return false;
        return original.call(level, pos, igniter);
    }

}
