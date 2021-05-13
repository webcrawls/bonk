package dev.kscott.bonk.bukkit.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utilities for interacting with players.
 */
public final class PlayerUtils {

    /**
     * The vector that represents a player who isn't moving.
     */
    private static final @NonNull Vector stillVector = new Vector(0, -0.0784000015258789, 0);

    /**
     * If the player is moving, returns {@code true}; otherwise {@code false}.
     *
     * @param player player
     * @return true for moving, false for not
     */
    public static boolean moving(final @NonNull Player player) {
        return !player.getVelocity().equals(stillVector);
    }

    /**
     * Calculates the knockback vector between two entities, with a given power.
     *
     * @param attacker the attacker entity
     * @param victim   the victim entity
     * @param power    the strength of the knockback - set to 1 for default
     * @return new calculated {@link Vector}
     */
    public static @NonNull Vector knockbackVector(
            final @NonNull Entity attacker,
            final @NonNull Entity victim,
            final double power
    ) {
        return victim.getLocation().toVector().subtract(attacker.getLocation().toVector())
                .normalize()
                .multiply(power);
    }

}
