package dev.kscott.bonk.bukkit.utils;

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

}
