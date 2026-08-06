package galena.nirvana.entity;

public interface ICustomCreeper {
    /**
     * @return true if the normal explosion should be replaced entirely (cancels vanilla's own
     *         {@code ServerLevel#createExplosion} call from inside {@code Creeper#explode}).
     */
    boolean customExplode(double x, double y, double z, float radius);
}
