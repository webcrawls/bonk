package dev.kscott.bonk.bukkit.player.death;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death from an unknown source.
 */
public class UnknownDeathCause implements DeathCause {

    @Override
    public @NonNull Component message() {
        return Component.text("%name% 404'd out of existence");
    }

}
