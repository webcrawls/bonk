package dev.kscott.bonk.bukkit.player.damage;

import dev.kscott.bonk.bukkit.player.PlayerService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

public interface DamageContext {

    static @NonNull DamageContext fromEvent(final @NonNull EntityDamageEvent event,
                                           final @NonNull PlayerService service) {

        if (!(event.getEntity() instanceof Player)) {
            throw new UnsupportedOperationException("Tried to create damage context with a non-Player damage event");
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            System.out.println("Found fall damage event");
            return FallDamageContext.fromEvent(event);
        }

        if (event instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
            if (entityDamageByEntityEvent.getDamager() instanceof Player &&
            entityDamageByEntityEvent.getEntity() instanceof Player) {
                System.out.println("Found player damage event");
                return PlayerDamageContext.fromEvent(entityDamageByEntityEvent);
            }
        }

        return GenericDamageContext.fromEvent(event);
    }

    double damage();

    @NonNull Player player();

    @NonNull Location location();

    long time();

}
