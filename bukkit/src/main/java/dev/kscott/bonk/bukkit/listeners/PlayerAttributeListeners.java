package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.log.LoggingService;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.powerup.GliderPowerup;
import dev.kscott.bonk.bukkit.weapon.WeaponService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on changes to player attributes (like health, hunger, etc)
 */
public class PlayerAttributeListeners implements Listener {

    private final @NonNull WeaponService weaponService;
    private final @NonNull LoggingService loggingService;
    private final @NonNull PlayerService playerService;
    private final @NonNull GliderPowerup gliderPowerup;

    /**
     * Constructs {@code PlayerAttributeListeners}.
     *
     * @param weaponService  the weapon service
     * @param loggingService the logging service
     * @param playerService  the player service
     */
    @Inject
    public PlayerAttributeListeners(
            final @NonNull WeaponService weaponService,
            final @NonNull LoggingService loggingService,
            final @NonNull GliderPowerup gliderPowerup,
            final @NonNull PlayerService playerService
    ) {
        this.weaponService = weaponService;
        this.gliderPowerup = gliderPowerup;
        this.playerService = playerService;
        this.loggingService = loggingService;
    }

    /**
     * Cancels food level changes.
     *
     * @param event {@link FoodLevelChangeEvent}
     */
    @EventHandler
    public void hunger(final @NonNull FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHandSwap(final @NonNull PlayerSwapHandItemsEvent event) {
        System.out.println(event.getPlayer());
    }

    @EventHandler
    public void playerDamage(final @NonNull EntityDamageEvent event) {
        final @NonNull Entity entity = event.getEntity();

        if (entity instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                // Jump boost absorbs a lot of damage
//                event.setDamage(event.getDamage() * 1.35);
            }
        }
    }

    @EventHandler
    public void playerDamaged(final @NonNull EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
            event.setCancelled(true);
        }

        this.playerService.handlePlayerDamage(event);
    }

    @EventHandler
    public void glideToggle(final @NonNull EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player player) {
            final boolean isGlideActivated = this.gliderPowerup.isGliding(player);

            System.out.println("Glide event executed, player was gliding: " + isGlideActivated);

            if (isGlideActivated) {
                event.setCancelled(true);
                player.setGliding(true);
                player.sendMessage(Component.text("Cancelled glide event & set to true"));
            } else {
                event.setCancelled(true);
            }
        }
    }

}
