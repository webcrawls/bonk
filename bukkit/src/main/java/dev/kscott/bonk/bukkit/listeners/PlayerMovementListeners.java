package dev.kscott.bonk.bukkit.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.inject.Inject;
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
     * Constructs {@link PlayerMovementListeners}.
     *
     * @param plugin the plugin
     */
    @Inject
    public PlayerMovementListeners(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

}
