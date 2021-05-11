package dev.kscott.bonk.api.player;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a player's position.
 *
 * @param x     the x coordinate
 * @param y     the y coordinate
 * @param z     the z coordinate
 * @param pitch the pitch of the player's view position
 * @param yaw   the yaw of the player's view position
 */
public record PlayerPosition(
        double x,
        double y,
        double z,
        double pitch,
        double yaw
) {
}
