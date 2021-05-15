package dev.kscott.bonk.bukkit.player.death;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.awt.*;

/**
 * Represents a death cause.
 */
public interface DeathCause {

    /**
     * Constructs a {@code DeathCause} using data from {@code event}.
     *
     * @param event death event
     * @return death cause
     */
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

    /**
     * Returns the message for this death. The victim's name is replaced with the %name% placeholder.
     *
     * @return death message
     */
    @NonNull Component message();

}
