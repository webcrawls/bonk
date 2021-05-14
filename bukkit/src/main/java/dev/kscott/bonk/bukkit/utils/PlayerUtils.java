package dev.kscott.bonk.bukkit.utils;

import dev.kscott.bonk.bukkit.game.Constants;
import org.bukkit.Location;
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
     * Calculates the knockback vector between two locations, with a given power.
     *
     * @param location1 the first location
     * @param location2 the second location
     * @param power     the strength of the knockback - set to 1 for default
     * @return new calculated {@link Vector}
     */
    public static @NonNull Vector knockbackVector(
            final @NonNull Location location1,
            final @NonNull Location location2,
            final double power
    ) {
        return location1.toVector().subtract(location2.toVector())
                .normalize()
                .multiply(power);
    }

    /**
     * Returns {@code true} if the player is near the ground, {@code false} if not.
     *
     * @param player player
     * @return boolean
     */
    public static boolean nearGround(final @NonNull Player player) {
        return player.getVelocity().getY() == Constants.Numbers.GRAVITY;
    }

}
