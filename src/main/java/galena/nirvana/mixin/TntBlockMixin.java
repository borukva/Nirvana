package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.block.ICustomTntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin {

    @Unique
    private boolean shouldPrime(BlockState state, Level world, BlockPos pos, @Nullable net.minecraft.core.Direction face, @Nullable LivingEntity igniter) {
        if (this instanceof ICustomTntBlock tnt) {
            tnt.onCaughtFire(state, world, pos, face, igniter);
            return false;
        }
        return true;
    }

    @WrapOperation(
            method = "onPlace",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean onBlockAdded(Level world, BlockPos pos, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state) {
        if (!shouldPrime(state, world, pos, null, null)) return false;
        return original.call(world, pos);
    }

    @WrapOperation(
            method = "neighborChanged",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean neighborUpdate(Level world, BlockPos pos, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state) {
        if (!shouldPrime(state, world, pos, null, null)) return false;
        return original.call(world, pos);
    }

    @WrapOperation(
            method = "playerWillDestroy",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean onBreak(Level world, BlockPos pos, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) Player player) {
        if (!shouldPrime(state, world, pos, null, player)) return false;
        return original.call(world, pos);
    }

    @WrapOperation(
            method = "useItemOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z")
    )
    private boolean onUseWithItem(Level world, BlockPos pos, LivingEntity igniter, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) BlockHitResult hit) {
        if (!shouldPrime(state, world, pos, hit.getDirection(), igniter)) return false;
        return original.call(world, pos, igniter);
    }

    @WrapOperation(
            method = "onProjectileHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/TntBlock;prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z")
    )
    private boolean onProjectileHit(Level world, BlockPos pos, LivingEntity igniter, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockState state, @Local(argsOnly = true) BlockHitResult hit) {
        if (!shouldPrime(state, world, pos, hit.getDirection(), igniter)) return false;
        return original.call(world, pos, igniter);
    }

    /**
     * Unlike the other pathways above, vanilla's own {@code wasExploded} (Yarn:
     * {@code onDestroyedByExplosion}) never calls {@code prime} internally - it builds a real
     * vanilla PrimedTnt by hand - so there's no shared call site to wrap here; the whole method is
     * replaced outright for custom TNT blocks. Without this, a nearby creeper or real TNT
     * destroying THC would still spawn genuine vanilla TNT regardless of what THC otherwise
     * disguises as.
     */
    @Inject(method = "wasExploded", at = @At("HEAD"), cancellable = true)
    private void onDestroyedByExplosion(ServerLevel world, BlockPos pos, Explosion explosion, CallbackInfo ci) {
        if (this instanceof ICustomTntBlock tnt) {
            tnt.wasExploded(world, pos, explosion);
            ci.cancel();
        }
    }
}
