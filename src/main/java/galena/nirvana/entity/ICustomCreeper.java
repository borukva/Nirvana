package galena.nirvana.entity;

public interface ICustomCreeper {
    /**
     * @return true if the normal explosion should be replaced entirely (cancels vanilla's own
     *         {@code ServerWorld#createExplosion} call from inside {@code CreeperEntity#explode}).
     */
    boolean customExplode(double x, double y, double z, float radius);
}
