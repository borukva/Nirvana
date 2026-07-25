package galena.nirvana.index;

import galena.nirvana.Nirvana;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class NirvanaSounds {
    public static SoundEvent SMOKING  = registerSoundEvent("item.use.smoking");
    public static SoundEvent BONG  = registerSoundEvent("item.use.bong");
    public static SoundEvent MUSIC_DISC_JAM  = registerSoundEvent("music_disc.jam");
    public static SoundEvent HERBAL_SALVE  = registerSoundEvent("item.use.herbal_salve");
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Nirvana.MOD_ID, name);
        return SoundEvent.of(id);
    }
}
