package dev.kscott.bonk.bukkit.player.death;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death caused by an Entity.
 *
 * @param player player who died
 * @param killer entity who killed {@code player}
 */
public record EntityDeathCause(
        @NonNull Player player,
        @NonNull Entity killer
) implements DeathCause {
}
