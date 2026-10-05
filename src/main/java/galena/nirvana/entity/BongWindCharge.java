package galena.nirvana.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NullMarked;

/** A wind-charge visual carrying a lingering cloud, with no impact damage or wind explosion. */
@NullMarked
public class BongWindCharge extends WindCharge implements PolymerEntity {
    private static final int MAX_FLIGHT_TICKS = 200;
    private PotionContents contents = PotionContents.EMPTY;

    public BongWindCharge(EntityType<? extends AbstractWindCharge> type, Level world) {
        super(type, world);
    }

    public BongWindCharge(ServerLevel world, LivingEntity owner, PotionContents contents) {
        this(NirvanaEntities.BONG_WIND_CHARGE, world);
        setOwner(owner);
        setPos(owner.getX(), owner.getEyeY(), owner.getZ());
        this.contents = contents;
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityTypes.WIND_CHARGE;
    }

    @Override
    @SuppressWarnings("resource") // level() borrows the entity's world; it must not be closed here.
    protected void onHit(HitResult hit) {
        // Do not call super: it deals direct damage and invokes block/explosion callbacks.
        if (hit.getType() == HitResult.Type.MISS || !(level() instanceof ServerLevel world)) return;
        var at = hit.getLocation();
        if (hit instanceof BlockHitResult blockHit) {
            // Keep the cloud just outside the impacted block rather than inside its collision face.
            at = at.add(Vec3.atLowerCornerOf(blockHit.getDirection().getUnitVec3i()).scale(0.25));
        }
        spawnCloud(world, at);
        discard();
    }

    private void spawnCloud(ServerLevel world, Vec3 at) {
        if (!contents.hasEffects()) return;
        var cloud = new AreaEffectCloud(world, at.x, at.y, at.z);
        if (getOwner() instanceof LivingEntity ownerEntity) {
            cloud.setOwner(ownerEntity);
        }
        cloud.setRadius(3.0F);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setDuration(600);
        cloud.setWaitTime(10);
        cloud.setRadiusPerTick(-cloud.getRadius() / cloud.getDuration());
        var potion = new ItemStack(Items.LINGERING_POTION);
        potion.set(DataComponents.POTION_CONTENTS, contents);
        cloud.applyComponentsFromItemStack(potion);
        world.addFreshEntity(cloud);

        // Cosmetic burst only: no explosion, damage, knockback or block interaction.
        world.sendParticles(ParticleTypes.GUST_EMITTER_SMALL, at.x, at.y, at.z, 1, 0, 0, 0, 0);
        world.playSound(null, at.x, at.y, at.z, SoundEvents.WIND_CHARGE_BURST,
                SoundSource.PLAYERS, 0.5F, 1.0F);
    }

    @Override
    protected void explode(Vec3 position) {
        // AbstractWindCharge also calls this above the world ceiling; only actual hits make clouds.
    }

    @Override
    @SuppressWarnings("resource") // The entity does not own its world's lifetime.
    public void tick() {
        if (!level().isClientSide() && tickCount >= MAX_FLIGHT_TICKS) {
            discard();
            return;
        }
        super.tick();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("potion_contents", PotionContents.CODEC, contents);
        output.putInt("FlightAge", tickCount);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        contents = input.read("potion_contents", PotionContents.CODEC).orElse(PotionContents.EMPTY);
        tickCount = input.getIntOr("FlightAge", 0);
    }
}
