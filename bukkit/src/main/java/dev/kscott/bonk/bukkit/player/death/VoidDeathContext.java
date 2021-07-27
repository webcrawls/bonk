package dev.kscott.bonk.bukkit.player.death;

import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death caused by falling into the void.
 */
public class VoidDeathContext implements DeathContext {

    public static @NonNull VoidDeathContext fromEvent(final @NonNull PlayerDeathEvent event) {
        final @NonNull Player player = event.getEntity();

        if (player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.VOID) {
            throw new UnsupportedOperationException("Cause must be VOID");
        }

        return new VoidDeathContext(player, player.getLocation(), System.currentTimeMillis());
    }

    private final @NonNull Player player;
    private final @NonNull Location location;
    private final long time;

    public VoidDeathContext(final @NonNull Player player,
                            final @NonNull Location location,
                            final long time) {
        this.player = player;
        this.location = location;
        this.time = time;
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
                        Component.text(" fell off")
                                .style(Styles.TEXT)
                )
                .build();
    }

}
