package dev.kscott.bonk.bukkit.player;

import dev.kscott.bonk.bukkit.position.GamePosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * The core bonk player class.
 */
public final class BonkPlayer {

    /**
     * The player associated with this {@code BonkPlayer}.
     */
    private final @NonNull Player player;

    /**
     * If true, the player is actively playing bonk.
     * If false, the player isn't playing, but still in bonk (i.e. in the lobby).
     */
    private @NonNull PlayerState state;

    /**
     * Constructs {@code BonkPlayer}.
     *
     * @param player the player to associate with this {@code BonkPlayer}
     */
    public BonkPlayer(final @NonNull Player player) {
        this.player = player;
        this.state = PlayerState.PRE_GAME;
    }

    /**
     * {@return the player's uuid}
     */
    public @NonNull UUID uuid() {
        return this.player.getUniqueId();
    }

    /**
     * {@return the player's name}
     */
    public @NonNull String name() {
        return this.player.getName();
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

    /**
     * {@return true if the player is playing (i.e. in the arena), false if the player isn't (i.e. in the lobby)}
     */
    public @NonNull PlayerState state() {
        return this.state;
    }

    /**
     * Sets the player's PlayerState.
     * <p>
     * Has no affect on the player other than modifying the internal value.
     *
     * @param state player state
     */
    public void state(final @NonNull PlayerState state) {
        this.state = state;
    }
}
