package dev.kscott.bonk.bukkit.player.death;

import dev.kscott.bonk.bukkit.lobby.LobbyService;
import dev.kscott.bonk.bukkit.player.BonkSpirit;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.player.damage.DamageContext;
import dev.kscott.bonk.bukkit.player.damage.PlayerDamageContext;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a death cause.
 */
public interface DeathContext {

    /**
     * Constructs a {@code DeathCause} using data from {@code event}.
     *
     * @param event death event
     * @return death cause
     */
    static @NonNull DeathContext fromEvent(final @NonNull PlayerDeathEvent event,
                                           final @NonNull PlayerService service) {
        final @NonNull Player player = event.getEntity();

        final @Nullable EntityDamageEvent damageEvent = player.getLastDamageCause();

        if (damageEvent == null) {
            return VoidDeathContext.fromEvent(event);
        }

        if (damageEvent instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            final @NonNull Entity damager = damageByEntityEvent.getDamager();

            if (damager instanceof Player) {
                return PlayerDeathContext.of(damageByEntityEvent, service);
            } else {
                return EntityDeathContext.of(damageByEntityEvent);
            }
        }

        if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final @NonNull BonkSpirit spirit = service.spirit(player);
            final @Nullable DamageContext latestDamage = spirit.latestDamage();

            if (latestDamage != null) {
                final @Nullable DamageContext previousDamage = spirit.previousDamage(latestDamage);

                if (previousDamage != null) {
                    if (previousDamage instanceof PlayerDamageContext playerDamageContext) {
                        System.out.println("Found launch death");
                        return new PlayerLaunchDeathContext(
                                service.spirit(playerDamageContext.player()),
                                service.spirit(playerDamageContext.attacker()),
                                damageEvent.getEntity().getLocation(),
                                playerDamageContext,
                                playerDamageContext.player().getFallDistance(),
                                System.currentTimeMillis()
                        );
                    }
                }
            }

        }

        return new UnknownDeathContext(player, player.getLocation(), System.currentTimeMillis());

    }

    @NonNull Player player();

    @NonNull Location location();

    /**
     * Returns the message for this death. The victim's name is replaced with the %name% placeholder.
     *
     * @return death message
     */
    @NonNull Component message();

    /**
     * Returns the Unix timestamp of when this death occurred.
     * @return the timestamp
     */
    long time();

}
