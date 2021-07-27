package dev.kscott.bonk.bukkit.player.damage;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerDamageContext implements DamageContext {

    private final @NonNull Player player;
    private final @NonNull Player attacker;
    private final @NonNull Location location;
    private final double damage;
    private final long time;

    public PlayerDamageContext(
            final @NonNull Player player,
            final @NonNull Player attacker,
            final double damage,
            final @NonNull Location location,
            final long time
    ) {
        this.player = player;
        this.attacker = attacker;
        this.location = location;
        this.damage = damage;
        this.time = time;
    }

    public static @NonNull PlayerDamageContext fromEvent(final @NonNull EntityDamageByEntityEvent event) {
        return new PlayerDamageContext(
                (Player) event.getEntity(),
                (Player) event.getDamager(),
                event.getFinalDamage(),
                event.getEntity().getLocation(),
                System.currentTimeMillis()
        );
    }

    public @NonNull Player attacker() {
        return this.attacker;
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
