package dev.kscott.bonk.bukkit.player.damage;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class FallDamageContext implements DamageContext {

    public static @NonNull FallDamageContext fromEvent(final @NonNull EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            throw new UnsupportedOperationException("Cause must be FALL");
        }

        final @NonNull Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            throw new UnsupportedOperationException("Entity must be a Player");
        }

        final @NonNull Player player = (Player) entity;

        final double finalDamage = event.getFinalDamage();
        final double fallDistance = player.getFallDistance();

        return new FallDamageContext(player, finalDamage, fallDistance, player.getLocation(), System.currentTimeMillis());
    }

    private final @NonNull Player player;
    private final @NonNull Location location;
    private final double damage;
    private final double distance;
    private final long time;

    public FallDamageContext(
            final @NonNull Player player,
            final double damage,
            final double fallDistance,
            final @NonNull Location location,
            final long time
    ) {
        this.player = player;
        this.damage = damage;
        this.distance = fallDistance;
        this.location = location;
        this.time = time;
    }

    public double distance() {
        return this.distance;
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
    public @NonNull Location location() {
        return this.location;
    }

    @Override
    public long time() {
        return this.time;
    }
}
