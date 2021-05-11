package dev.kscott.bonk.bukkit.player;

import dev.kscott.bonk.bukkit.position.GamePosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The core bonk player class.
 */
public final class BonkPlayer {

    /**
     * The player associated with this {@code BonkPlayer}.
     */
    private final @NonNull Player player;

    /**
     * Constructs {@code BonkPlayer}.
     *
     * @param player the player to associate with this {@code BonkPlayer}
     */
    public BonkPlayer(final @NonNull Player player) {
        this.player = player;
    }

    /**
     * {@return the player's position}
     */
    public @NonNull GamePosition position() {
        final @NonNull Location location = this.player.getLocation();

        return new GamePosition(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw()
        );
    }

    /**
     * Sets the player's position.
     *
     * @param position desired position
     */
    public void position(final @NonNull GamePosition position) {
        this.player.teleport(new Location(
                this.player.getWorld(),
                position.x(),
                position.y(),
                position.z(),
                position.yaw(),
                position.pitch()
        ));
    }

}
