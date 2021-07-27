package dev.kscott.bonk.bukkit.player.death;

import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death caused by an Entity.
 */
public class EntityDeathContext implements DeathContext {

    private final @NonNull Player player;
    private final @NonNull Entity killer;
    private final @NonNull Location location;
    private final long time;

    public EntityDeathContext(final @NonNull Player player,
                              final @NonNull Entity killer,
                              final @NonNull Location location,
                              final long time) {
        this.player = player;
        this.killer = killer;
        this.location = location;
        this.time = time;
    }

    public static @NonNull EntityDeathContext of(final @NonNull EntityDamageByEntityEvent event) {
        return new EntityDeathContext(
                (Player) event.getEntity(),
                event.getDamager(),
                event.getEntity().getLocation(),
                System.currentTimeMillis()
        );
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
                .append(Component.text(" ⚔ "))
                .append(
                        this.player.displayName()
                                .style(Styles.TEXT)
                                .color(Colours.RED_LIGHT))
                .build();
    }

}
