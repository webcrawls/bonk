package dev.kscott.bonk.bukkit.player;

import dev.kscott.bonk.api.player.BonkPlayer;
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
    public @NonNull String getUsername() {
        return this.player.getName();
    }

    /**
     * {@return the health of the player}
     */
    @Override
    public double getHealth() {
        return this.player.getHealth();
    }

    /**
     * {@return if the player is playing or not}
     */
    @Override
    public boolean isPlaying() {
        // TODO implement playing check
        return false;
    }

    /**
     * {@return the player's current weapon}
     */
    @Override
    public @Nullable BonkWeapon getCurrentWeapon() {
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
}
