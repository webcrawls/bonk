package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
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
import org.jetbrains.annotations.NotNull;

/**
 * Listens on changes to player attributes (like health, hunger, etc)
 */
public class PlayerAttributeListeners implements Listener {

    /**
     * The weapon service.
     */
    private final @NonNull WeaponService weaponService;

    /**
     * Constructs {@code PlayerAttributeListeners}.
     *
     * @param weaponService the weapon service
     */
    @Inject
    public PlayerAttributeListeners(
            final @NonNull WeaponService weaponService
    ) {
        this.weaponService = weaponService;
    }

    /**
     * Cancels food level changes.
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
            final @Nullable ItemStack weaponItem = attackerPlayer.getInventory().getItemInMainHand();

            // This very much can be true
            if (weaponItem == null) {
                return;
            }

            final @Nullable Weapon weapon = this.weaponService.weaponFromItemStack(weaponItem);

            if (weapon == null) {
                return;
            }

            // We are dealing with a textbook bonk hit. Launch accordingly.
            final @NonNull Vector velocity = victimPlayer.getVelocity();

            if (PlayerUtils.moving(victimPlayer)) {
                velocity.multiply(2.3);
            } else {
                velocity.multiply(3);
            }

            victimPlayer.setVelocity(velocity);
        }
    }

}
