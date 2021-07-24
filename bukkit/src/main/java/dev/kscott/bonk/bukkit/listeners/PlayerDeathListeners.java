package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.player.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on player death-related events.
 */
public class PlayerDeathListeners implements Listener {

    /**
     * The player service.
     */
    private final @NonNull PlayerService playerService;

    /**
     * Constructs {@code PlayerDeathListener}.
     *
     * @param playerService the player service
     */
    @Inject
    public PlayerDeathListeners(final @NonNull PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Handles the player death event.
     *
     * @param event {@link PlayerDeathEvent}
     */
    @EventHandler
    public void playerDeath(final @NonNull PlayerDeathEvent event) {
        this.playerService.handlePlayerDeath(event);
    }


}
