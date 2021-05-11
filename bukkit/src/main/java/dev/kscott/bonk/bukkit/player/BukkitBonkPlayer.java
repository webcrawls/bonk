package dev.kscott.bonk.bukkit.player;

import dev.kscott.bonk.api.player.BonkPlayer;
import dev.kscott.bonk.api.player.PlayerPosition;
import dev.kscott.bonk.api.weapon.BonkWeapon;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * The BukkitBonkPlayer.
 */
public class BukkitBonkPlayer implements BonkPlayer {

    private @NonNull PlayerPosition position;

    /**
     * The {@link Player} attached to this class.
     */
    private final @NonNull Player player;

    /**
     * Constructs {@code BukkitBonkPlayer}.
     *
     * @param player the player
     */
    public BukkitBonkPlayer(final @NonNull Player player) {
        this.player = player;
    }

    /**
     * {@return the uuid}
     */
    @Override
    public @NonNull UUID uuid() {
        return this.player.getUniqueId();
    }

    /**
     * {@return the name}
     */
    @Override
    public @NonNull String username() {
        return this.player.getName();
    }

    /**
     * {@return the health of the player}
     */
    @Override
    public double health() {
        return this.player.getHealth();
    }

    /**
     * {@return if the player is playing or not}
     */
    @Override
    public boolean playing() {
        // TODO implement playing check
        return false;
    }

    /**
     * {@return the player's current weapon}
     */
    @Override
    public @Nullable BonkWeapon weapon() {
        // TODO weapons
        return null;
    }

    /**
     * {@return the audience}
     */
    @Override
    public @NonNull Audience audience() {
        return this;
    }

    /**
     * {@return the player's position}
     */
    @Override
    public @NonNull PlayerPosition position() {
        return this.position;
    }

    /**
     * Sets the player's position.
     *
     * @param position desired position
     */
    @Override
    public void position(final @NonNull PlayerPosition position) {
        // TODO: Teleport player
        this.position = position;
    }
}
