package galena.nirvana.world.entity;

import galena.nirvana.index.NirvanaEffects;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.mixins.CreeperAccessor;
import galena.nirvana.platform.Services;
import galena.nirvana.world.THCCloud;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Reefer extends Creeper implements ICustomCreeper {

    public Reefer(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Replace default player targeting: only aggro players without sufficient Peace effect
        this.targetSelector.removeAllGoals(g -> g instanceof NearestAttackableTargetGoal);
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(this, Player.class, true,
            (entity, serverLevel) -> {
                if (!(entity instanceof Player player)) return false;
                var effect = player.getEffect(NirvanaEffects.peaceHolder());
                return effect == null || effect.getAmplifier() < Services.CONFIG.common().reeferAfterHits();
            }
        ));
    }

    @Override
    public boolean customExplode(double x, double y, double z, float radius) {
        THCCloud.spawnCloud(level(), position(), 1F, 30, 30);
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean bl) {
        var cause = source.getEntity();
        // canDropMobsSkull()/increaseDroppedSkulls() were removed in 1.21.9; CreeperAccessor
        // reproduces the same one-skull-per-explosion cap via the underlying droppedSkulls flag.
        if (cause != this && cause instanceof Creeper creeper && creeper.isPowered()) {
            var accessor = (CreeperAccessor) creeper;
            if (!accessor.getDroppedSkulls()) {
                accessor.setDroppedSkulls(true);
                spawnAtLocation(level, NirvanaItems.REEFER_HEAD.get());
            }
        }
    }

}
