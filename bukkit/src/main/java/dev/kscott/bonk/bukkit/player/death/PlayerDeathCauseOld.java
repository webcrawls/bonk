package dev.kscott.bonk.bukkit.player.death;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a Bonk player's death cause.
 */
public enum PlayerDeathCauseOld {

    /**
     * When a player is killed by another player.
     */
    PLAYER,
    /**
     * When a player is killed by fall damage.
     */
    FALL,
    /**
     * When a player is killed by falling into the void.
     */
    VOID,
    /**
     * When the death cause couldn't be determined/isn't important.
     */
    UNKNOWN;

    /**
     * Determines the {@link PlayerDeathCauseOld} from a {@link PlayerDeathEvent}.
     *
     * @param event event
     * @return cause
     */
    public static @NonNull PlayerDeathCauseOld cause(final @NonNull PlayerDeathEvent event) {
        final @Nullable EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();

        if (damageEvent == null) {
            return UNKNOWN;
        }

        if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
            return FALL;
        }

        if (damageEvent instanceof EntityDamageByEntityEvent edbeEvent) {
            if (edbeEvent.getDamager() instanceof Player) {
                return PLAYER;
            }
        }

        return UNKNOWN;
    }

}
