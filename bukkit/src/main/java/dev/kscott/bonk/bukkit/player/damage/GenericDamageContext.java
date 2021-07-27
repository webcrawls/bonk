package dev.kscott.bonk.bukkit.player.damage;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GenericDamageContext implements DamageContext {

    private final @NonNull Player player;
    private final @NonNull Location location;
    private final double damage;
    private final long time;
    public GenericDamageContext(final @NonNull Player player,
                                final double damage,
                                final @NonNull Location location,
                                final long time) {
        this.player = player;
        this.damage = damage;
        this.time = time;
        this.location = location;
    }

    public static @NonNull GenericDamageContext fromEvent(final @NonNull EntityDamageEvent event) {
        final @NonNull Player player = (Player) event.getEntity();

        return new GenericDamageContext(player, event.getFinalDamage(), player.getLocation(), System.currentTimeMillis());
    }

    @Override
    public double damage() {
        return this.damage;
    }

    @Override
    public @NonNull Player player() {
        return this.player;
    }

    @Override
    public long time() {
        return this.time;
    }

    @Override
    public @NonNull Location location() {
        return this.location;
    }

}
