package galena.nirvana.entity;

public interface ICustomTnt {
    /**
     * @return true if the normal explosion should be replaced entirely (cancels vanilla's own
     *         {@code World#createExplosion} call from inside {@code TntEntity#explode}).
     */
    boolean customExplode(double x, double y, double z, float power);
}
