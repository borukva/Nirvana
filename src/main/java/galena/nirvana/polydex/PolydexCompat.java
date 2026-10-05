package galena.nirvana.polydex;

import net.fabricmc.loader.api.FabricLoader;

/** Keeps all Polydex API references out of classes loaded without the optional mod. */
public final class PolydexCompat {
    private PolydexCompat() {
    }

    public static void register() {
        if (FabricLoader.getInstance().isModLoaded("polydex")) {
            PolydexCompatImpl.register();
        }
    }
}
