package dev.kscott.bonk.bukkit.position;

/**
 * Represents a position in the game.
 *
 * @param x     the x coordinate
 * @param y     the y coordinate
 * @param z     the z coordinate
 * @param yaw   the yaw
 * @param pitch the pitch
 */
public final record GamePosition(
        double x,
        double y,
        double z,
        float yaw,
        float pitch
) {
}
