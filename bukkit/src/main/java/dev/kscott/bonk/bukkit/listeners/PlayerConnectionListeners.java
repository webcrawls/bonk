package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.player.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on player join events.
 */
public final class PlayerConnectionListeners implements Listener {

    private final @NonNull PlayerService playerService;

    /**
     * Constructs PlayerJoinListener.
     *
     * @param playerService the PlayerService
     */
    @Inject
    public PlayerConnectionListeners(final @NonNull PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Handles player joins.
     *
     * @param event {@link PlayerJoinEvent}
     */
    @EventHandler
    public void playerJoin(final @NonNull PlayerJoinEvent event) {
        this.playerService.joined(event);
    }

    /**
     * Handles player quits.
     *
     * @param event {@link PlayerQuitEvent}
     */
    @EventHandler
    public void playerLeave(final @NonNull PlayerQuitEvent event) {
        this.playerService.quit(event);
    }
}
