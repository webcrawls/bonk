package dev.kscott.bonk.bukkit.position;

/**
 * Represents a position in the game.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @param z the z coordinate
 * @param pitch the pitch
 * @param yaw the yaw
 */
public record GamePosition(
        double x,
        double y,
        double z,
        float pitch,
        float yaw
) {
}
