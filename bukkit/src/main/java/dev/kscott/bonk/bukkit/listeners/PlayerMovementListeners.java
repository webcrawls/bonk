package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.player.DoubleJumpService;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.player.death.VoidDeathContext;
import dev.kscott.bonk.bukkit.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
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
     * The double jump service.
     */
    private final @NonNull DoubleJumpService doubleJumpService;

    /**
     * Constructs {@link PlayerMovementListeners}.
     *
     * @param plugin            the plugin
     * @param playerService     the player service
     * @param doubleJumpService the double jump service
     */
    @Inject
    public PlayerMovementListeners(
            final @NonNull JavaPlugin plugin,
            final @NonNull PlayerService playerService,
            final @NonNull DoubleJumpService doubleJumpService
    ) {
        this.plugin = plugin;
        this.doubleJumpService = doubleJumpService;
        this.playerService = playerService;
    }

    /**
     * Processes all {@link PlayerMoveEvent} events.
     */
    @EventHandler
    public void playerMoveEvent(final @NonNull PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) return;
        final @NonNull Player player = event.getPlayer();
        final @NonNull Location from = event.getFrom();

        // Reset player if y >= 0
        if (from.getBlockY() <= 1) {
            this.playerService.handlePlayerDeath(new VoidDeathContext(player, from, System.currentTimeMillis()));
            return;
        }

    }

    /**
     * Handles the double jump.
     *
     * @param event event
     */
    @EventHandler
    public void playerShift(final @NonNull PlayerToggleSneakEvent event) {
        final @NonNull Player player = event.getPlayer();

        if (!this.playerService.inGame(player)) {
            return;
        }

        if (!PlayerUtils.isNearGround(player) && PlayerUtils.movingY(player)) {
            if (this.doubleJumpService.canDoubleJump(player)) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5F, 1F);
                this.doubleJumpService.doubleJump(player);
            }
        }
    }

}
