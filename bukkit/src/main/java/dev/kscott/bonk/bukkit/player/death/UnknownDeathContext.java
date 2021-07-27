package dev.kscott.bonk.bukkit.player.death;

import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death from an unknown source.
 */
public class UnknownDeathContext implements DeathContext {

    private final @NonNull Player player;
    private final @NonNull Location location;
    private final long time;

    public UnknownDeathContext(
            final @NonNull Player player,
            final @NonNull Location location,
            final long time
    ) {
        this.player = player;
        this.time = time;
        this.location = location;
    }

    @Override
    public long time() {
        return this.time;
    }

    @Override
    public @NonNull Player player() {
        return this.player;
    }

    @Override
    public @NonNull Location location() {
        return this.location;
    }

    @Override
    public @NonNull Component message() {
        return Component.text()
                .append(
                        this.player.displayName()
                                .style(Styles.TEXT)
                                .color(Colours.BLUE_LIGHT)
                )
                .append(
                        Component.text(" 404'd.")
                                .style(Styles.TEXT)
                )
                .build();
    }

}
