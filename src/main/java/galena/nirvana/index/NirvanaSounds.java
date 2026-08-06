package galena.nirvana.index;

import galena.nirvana.Nirvana;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;

public class NirvanaSounds {
    public static SoundEvent SMOKING  = registerSoundEvent("item.use.smoking");
    public static SoundEvent BONG  = registerSoundEvent("item.use.bong");
    /** Played a beat after a hit, when the smoke actually comes back out. */
    public static SoundEvent BLOW  = registerSoundEvent("item.use.blow");
    /** Played manually (see {@link galena.nirvana.mixin.JukeboxBlockEntityMixin}) whenever a
     * jukebox is actually playing {@link NirvanaItems#MUSIC_DISC_JAM} - the jukebox_song entry
     * itself points at vanilla's silent {@code intentionally_empty} sound instead, since a
     * genuinely custom sound_event id there would need every connecting client (not just this
     * server) to already know it, which defeats a Polymer mod's whole point. */
    public static SoundEvent MUSIC_DISC_JAM  = registerSoundEvent("music_disc.jam");
    public static SoundEvent HERBAL_SALVE  = registerSoundEvent("item.use.herbal_salve");
    /**
     * Deliberately NOT {@link net.minecraft.core.Registry#register}ed: {@code SoundEvent} is
     * one of the "static" registries Fabric's own registry sync tracks, so a real registration
     * there marks the mod as required for any connecting client - exactly what a Polymer mod (no
     * client mod needed) can't afford. A bare, unregistered {@link SoundEvent#of} still resolves
     * fine wherever an identifier is enough to look a sound up by name (playSound calls) - just
     * not for data resolved through a live registry, like a jukebox_song's own sound_event field.
     */
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, name);
        return SoundEvent.createVariableRangeEvent(id);
    }
}
