package galena.nirvana.mixin;

import galena.nirvana.Nirvana;
import galena.nirvana.index.NirvanaSounds;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The "Jam" disc's own jukebox_song entry points at vanilla's silent {@code intentionally_empty}
 * sound rather than a real custom one (see {@link NirvanaSounds#MUSIC_DISC_JAM} for why), so the
 * actual audio has to be played by hand here whenever a jukebox ends up holding that disc - the
 * same "just call playSound with an unregistered SoundEvent" trick every other custom sound in
 * this mod already relies on.
 */
@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin {
    private static final RegistryKey<JukeboxSong> JAM = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Nirvana.MOD_ID, "jam"));

    @Unique
    private boolean nirvana$playingJam = false;

    @Inject(method = "setStack", at = @At("TAIL"))
    private void nirvana$onSetStack(ItemStack stack, CallbackInfo ci) {
        JukeboxBlockEntity self = (JukeboxBlockEntity) (Object) this;
        if (!(self.getWorld() instanceof ServerWorld world)) {
            return;
        }

        boolean isJam = JukeboxSong.getSongEntryFromStack(world.getRegistryManager(), stack)
                .map(entry -> entry.matchesKey(JAM))
                .orElse(false);

        if (isJam && !this.nirvana$playingJam) {
            world.playSound(null, self.getPos(), NirvanaSounds.MUSIC_DISC_JAM, SoundCategory.RECORDS, 4.0F, 1.0F);
        } else if (!isJam && this.nirvana$playingJam) {
            var packet = new StopSoundS2CPacket(NirvanaSounds.MUSIC_DISC_JAM.id(), SoundCategory.RECORDS);
            for (var player : world.getPlayers()) {
                player.networkHandler.sendPacket(packet);
            }
        }
        this.nirvana$playingJam = isJam;
    }
}
