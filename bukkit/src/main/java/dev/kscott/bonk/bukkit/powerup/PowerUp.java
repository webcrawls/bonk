package dev.kscott.bonk.bukkit.powerup;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Bonk power up.
 */
public interface PowerUp {

    /**
     * Activates this power up for {@code player}.
     *
     * @param player player
     */
    void activate(final @NonNull Player player);

}
