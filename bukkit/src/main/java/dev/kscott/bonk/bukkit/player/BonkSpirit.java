package dev.kscott.bonk.bukkit.player;

import dev.kscott.bonk.bukkit.position.GamePosition;
import dev.kscott.bonk.bukkit.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * The core bonk player class.
 */
public final class BonkSpirit {

    private final @NonNull Player player;
    private @NonNull Weapon weapon;
    private @Nullable Entity lastAttacker;
    private long lastAttackTime;

    /**
     * Constructs {@code BonkPlayer}.
     *
     * @param player the player to associate with this {@code BonkPlayer}
     * @param weapon the weapon to give the player
     */
    public BonkSpirit(
            final @NonNull Player player,
            final @NonNull Weapon weapon
    ) {
        this.player = player;
        this.weapon = weapon;
        this.lastAttacker = null;
        this.lastAttackTime = 0;
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
                position.pitch(),
                position.yaw()
        ));
    }

    /**
     * {@return the player's selected weapon}
     */
    public @NonNull Weapon weapon() {
        return this.weapon;
    }

    /**
     * Sets the player's weapon.
     *
     * @param weapon the weapon
     */
    public void weapon(final @NonNull Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * {@return the player}
     */
    public @NonNull Player player() {
        return this.player;
    }

    /**
     * {@return this player's last attacker}
     */
    public @Nullable Entity lastAttacker() {
        return this.lastAttacker;
    }

    /**
     * {@return when this player was last attacked}
     */
    public long lastAttackTime() {
        return this.lastAttackTime;
    }

    /**
     * Sets this player's last attacker.
     *
     * @param lastAttacker entity
     */
    public void lastAttacker(final @NonNull Entity lastAttacker) {
        this.lastAttacker = lastAttacker;
    }

    /**
     * Sets this player's last attack time.
     *
     * @param lastAttackTime last attack time (in ms, as unix timestamp)
     */
    public void lastAttackTime(final long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
}
