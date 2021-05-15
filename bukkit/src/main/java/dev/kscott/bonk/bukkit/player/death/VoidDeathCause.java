package dev.kscott.bonk.bukkit.player.death;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death caused by falling into the void.
 *
 * @param player player who died
 */
public record VoidDeathCause(
        @NonNull Player player
) implements DeathCause {
}
