package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.log.LoggingService;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.utils.PlayerUtils;
import dev.kscott.bonk.bukkit.weapon.Weapon;
import dev.kscott.bonk.bukkit.weapon.WeaponService;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Listens on changes to player attributes (like health, hunger, etc)
 */
public class PlayerAttributeListeners implements Listener {

    /**
     * The weapon service.
     */
    private final @NonNull WeaponService weaponService;

    /**
     * The logging service.
     */
    private final @NonNull LoggingService loggingService;

    /**
     * The player service.
     */
    private final @NonNull PlayerService playerService;

    /**
     * Constructs {@code PlayerAttributeListeners}.
     *
     * @param weaponService  the weapon service
     * @param loggingService the logging service
     * @param playerService the player service
     */
    @Inject
    public PlayerAttributeListeners(
            final @NonNull WeaponService weaponService,
            final @NonNull LoggingService loggingService,
            final @NonNull PlayerService playerService
    ) {
        this.weaponService = weaponService;
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
    public void playerDamage(final @NonNull EntityDamageEvent event) {
        final @NonNull Entity entity = event.getEntity();

        if (entity instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                // Jump boost absorbs a lot of damage
                event.setDamage(event.getDamage() * 1.35);
            }
        }
    }

    @EventHandler
    public void playerDamagedByPlayer(final @NonNull EntityDamageByEntityEvent event) {
        final @NonNull Entity attacker = event.getDamager();
        final @NonNull Entity victim = event.getEntity();

        // Only interested in events involving two players
        if (attacker instanceof final @NonNull Player attackerPlayer &&
                victim instanceof final Player victimPlayer) {
            this.playerService.attacked(attackerPlayer, victimPlayer);
        }
    }

}
