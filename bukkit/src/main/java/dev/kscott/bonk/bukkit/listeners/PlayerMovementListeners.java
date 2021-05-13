package dev.kscott.bonk.bukkit.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on player movement-related events.
 */
public class PlayerMovementListeners implements Listener {

    /**
     * Makes player jumps a bit higher.
     *
     * @param event {@link PlayerJumpEvent}
     */
    @EventHandler
    public void handlePlayerJump(final @NonNull PlayerJumpEvent event) {
        final @NonNull Player player = event.getPlayer();

        final @NonNull Vector jumpVelocity = player.getVelocity();

        jumpVelocity.normalize().multiply(new Vector(1, 1.2, 1));

        player.setVelocity(player.getVelocity());
    }

}
