package dev.kscott.bonk.bukkit.player;

import dev.kscott.bonk.bukkit.position.GamePosition;
import dev.kscott.bonk.bukkit.weapon.Weapon;
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
     * The weapon the player is using. Defaults to Stick.
     */
    private @NonNull
    final Weapon weapon;

    /**
     * Constructs {@code BonkPlayer}.
     *
     * @param player the player to associate with this {@code BonkPlayer}
     * @param weapon the weapon to give the player
     */
    public BonkPlayer(
            final @NonNull Player player,
            final @NonNull Weapon weapon
    ) {
        this.player = player;
        this.weapon = weapon;
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
     * {@return the player's selected weapon}
     */
    public @NonNull Weapon weapon() {
        return this.weapon;
    }
}
