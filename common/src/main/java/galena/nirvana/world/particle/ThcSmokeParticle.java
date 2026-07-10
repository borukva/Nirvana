package galena.nirvana.world.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class ThcSmokeParticle extends SingleQuadParticle {

    public ThcSmokeParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.get(level.random));
        setAlpha(0.9F);
        scale(3F);
        setSize(0.25F, 0.25F);
        setLifetime(random.nextInt(50) + 100);
        this.yd = (random.nextFloat() + 1F) / 100F;
        this.xd = (random.nextFloat() - 0.5F) / 10F;
        this.zd = (random.nextFloat() - 0.5F) / 10F;
        this.yo += level.random.nextFloat() * 1.5;
        setPos(x, yo, z);
    }

    public void tick() {
        super.tick();
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, net.minecraft.util.RandomSource random) {
            return new ThcSmokeParticle(level, x, y, z, sprites);
        }
    }

}
