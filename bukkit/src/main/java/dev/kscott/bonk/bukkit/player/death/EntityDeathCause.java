package dev.kscott.bonk.bukkit.player.death;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death caused by an Entity.
 *
 * @param killer entity who killed {@code player}
 */
public record EntityDeathCause(
        @NonNull Entity killer
) implements DeathCause {

    @Override
    public @NonNull Component message() {
        return Component.text("%name% was killed by ")
                .append(Component.text(killer.getName()));
    }

}
