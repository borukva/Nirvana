package galena.nirvana.world.particle;

import galena.nirvana.index.NirvanaParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SmokeRingParticle extends SingleQuadParticle implements ICollidingParticle {

    private final SpriteSet sprites;

    protected SmokeRingParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.get(level.random));
        this.sprites = sprites;
        this.friction = 0.98F;
        setLifetime(20 * 8);
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
        alpha = 0.9F - age * 0.5F / lifetime;
    }

    @Override
    public void collide() {
        remove();
        level.addParticle(NirvanaParticles.THC_SMOKE.get(), x, y, z, 0, 0, 0);
    }

    @Override
    public void setSpriteFromAge(SpriteSet sprites) {
        if (removed) return;
        setSprite(sprites.get(Math.min((int) (Math.sqrt(age) * Math.sqrt(lifetime) * 2), lifetime), lifetime));
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
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, net.minecraft.util.RandomSource random) {
            var particle = new SmokeRingParticle(level, x, y, z, sprites);
            particle.xd = xd;
            particle.yd = yd;
            particle.zd = zd;
            return particle;
        }
    }

}
