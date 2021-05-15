package dev.kscott.bonk.bukkit.player.death;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death caused by falling into the void.
 */
public record VoidDeathCause() implements DeathCause {

    @Override
    public @NonNull Component message() {
        return Component.text("%name% took a tumble into the void");
    }

}
