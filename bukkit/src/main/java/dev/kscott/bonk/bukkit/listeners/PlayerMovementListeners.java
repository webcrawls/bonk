package dev.kscott.bonk.bukkit.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.player.PlayerDeathCause;
import dev.kscott.bonk.bukkit.player.PlayerService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on player movement-related events.
 */
public class PlayerMovementListeners implements Listener {

    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * The player service.
     */
    private final @NonNull PlayerService playerService;

    /**
     * Constructs {@link PlayerMovementListeners}.
     *
     * @param plugin the plugin
     * @param playerService the player service
     */
    @Inject
    public PlayerMovementListeners(
            final @NonNull JavaPlugin plugin,
            final @NonNull PlayerService playerService
    ) {
        this.plugin = plugin;
        this.playerService = playerService;
    }

    /**
     * Processes all {@link PlayerMoveEvent}-related data.
     */
    @EventHandler
    public void playerMoveEvent(final @NonNull PlayerMoveEvent event) {
        final @NonNull Player player = event.getPlayer();
        final @NonNull Location from = event.getFrom();

        // Reset player if y >= 0
        if (from.getBlockY() <= 1) {
            this.playerService.died(player, PlayerDeathCause.VOID);
            return;
        }

    }

}
