package dev.kscott.bonk.bukkit.player.death;

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
public interface DeathCause {

    static @NonNull DeathCause fromEvent(final @NonNull PlayerDeathEvent event) {
        final @NonNull Player player = event.getEntity();

        final @Nullable EntityDamageEvent damageEvent = player.getLastDamageCause();

        if (damageEvent == null) {
            return new VoidDeathCause();
        }

        if (damageEvent instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            return new EntityDeathCause(damageByEntityEvent.getDamager());
        }

        return new UnknownDeathCause();

    }

}
