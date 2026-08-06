package galena.nirvana.mixin;

import galena.nirvana.Nirvana;
import galena.nirvana.index.NirvanaSounds;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
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
    private static final ResourceKey<JukeboxSong> JAM = ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "jam"));

    @Unique
    private boolean nirvana$playingJam = false;

    @Inject(method = "setTheItem", at = @At("TAIL"))
    private void nirvana$onSetStack(ItemStack stack, CallbackInfo ci) {
        JukeboxBlockEntity self = (JukeboxBlockEntity) (Object) this;
        if (!(self.getLevel() instanceof ServerLevel world)) {
            return;
        }

        boolean isJam = JukeboxSong.fromStack(stack)
                .map(entry -> entry.is(JAM))
                .orElse(false);

        if (isJam && !this.nirvana$playingJam) {
            world.playSound(null, self.getBlockPos(), NirvanaSounds.MUSIC_DISC_JAM, SoundSource.RECORDS, 4.0F, 1.0F);
        } else if (!isJam && this.nirvana$playingJam) {
            var packet = new ClientboundStopSoundPacket(NirvanaSounds.MUSIC_DISC_JAM.location(), SoundSource.RECORDS);
            for (var player : world.players()) {
                player.connection.send(packet);
            }
        }
        this.nirvana$playingJam = isJam;
    }
}
