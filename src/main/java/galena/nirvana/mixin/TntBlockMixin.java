package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.block.ICustomTntBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin {

    @Unique
    private boolean shouldPrime(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.math.Direction face, @Nullable LivingEntity igniter) {
        if (this instanceof ICustomTntBlock tnt) {
            tnt.onCaughtFire(state, world, pos, face, igniter);
            return false;
        }
        return true;
    }

    @WrapOperation(
            method = "onBlockAdded",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TntBlock;primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z")
    )
    private boolean onBlockAdded(World world, BlockPos pos, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state) {
        if (!shouldPrime(state, world, pos, null, null)) return false;
        return original.call(world, pos);
    }

    @WrapOperation(
            method = "neighborUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TntBlock;primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z")
    )
    private boolean neighborUpdate(World world, BlockPos pos, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state) {
        if (!shouldPrime(state, world, pos, null, null)) return false;
        return original.call(world, pos);
    }

    @WrapOperation(
            method = "onBreak",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TntBlock;primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z")
    )
    private boolean onBreak(World world, BlockPos pos, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) PlayerEntity player) {
        if (!shouldPrime(state, world, pos, null, player)) return false;
        return original.call(world, pos);
    }

    @WrapOperation(
            method = "onUseWithItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TntBlock;primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z")
    )
    private boolean onUseWithItem(World world, BlockPos pos, LivingEntity igniter, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) BlockHitResult hit) {
        if (!shouldPrime(state, world, pos, hit.getSide(), igniter)) return false;
        return original.call(world, pos, igniter);
    }

    @WrapOperation(
            method = "onProjectileHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TntBlock;primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z")
    )
    private boolean onProjectileHit(World world, BlockPos pos, LivingEntity igniter, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) BlockHitResult hit) {
        if (!shouldPrime(state, world, pos, hit.getSide(), igniter)) return false;
        return original.call(world, pos, igniter);
    }
}
