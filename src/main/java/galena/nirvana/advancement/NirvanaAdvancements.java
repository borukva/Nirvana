package galena.nirvana.advancement;

import galena.nirvana.effects.NirvanaEffects;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NirvanaAdvancements {
    public static final PeaceDurationCriterion PEACE_DURATION_CRITERION =
            Criteria.register("peace_duration", new PeaceDurationCriterion());

    private static final Map<UUID, Integer> playerJumpTimers = new ConcurrentHashMap<>();

    public static void register() {

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID playerUuid = player.getUuid();

                if (player.hasStatusEffect(NirvanaEffects.PEACE)) {
                    // Player has the effect, increment their timer
                    int currentTicks = playerJumpTimers.getOrDefault(playerUuid, 0) + 1;
                    playerJumpTimers.put(playerUuid, currentTicks);

                    // Check if the timer has reached a point where it could trigger an advancement
                    // We call our custom trigger here
                    PEACE_DURATION_CRITERION.trigger(player, currentTicks);

                } else {
                    // Player does not have the effect, reset their timer to 0
                    if (playerJumpTimers.containsKey(playerUuid)) {
                        playerJumpTimers.put(playerUuid, 0);
                    }
                }
            }
        });

        // Clean up the map when a player disconnects to prevent memory leaks
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            playerJumpTimers.remove(handler.getPlayer().getUuid());
        });
    }
}
