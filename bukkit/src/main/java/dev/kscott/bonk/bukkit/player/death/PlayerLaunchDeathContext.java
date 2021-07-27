package dev.kscott.bonk.bukkit.player.death;

import dev.kscott.bonk.bukkit.player.BonkSpirit;
import dev.kscott.bonk.bukkit.player.damage.DamageContext;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerLaunchDeathContext extends PlayerDeathContext {

    private final @NonNull DamageContext launchDamageContext;
    private final double distance;

    /**
     * Constructs {@code PlayerLaunchDeathContext}.
     *
     * @param playerSpirit the player
     * @param killerSpirit the killer
     * @param location the location
     * @param time the time
     */
    public PlayerLaunchDeathContext(
            final @NonNull BonkSpirit playerSpirit,
            final @NonNull BonkSpirit killerSpirit,
            final @NonNull Location location,
            final @NonNull DamageContext launchDamageContext,
            final double distance,
            final long time) {
        super(playerSpirit, killerSpirit, location, time);
        this.launchDamageContext = launchDamageContext;

        this.distance = distance;
    }

    public @NonNull DamageContext launchDamage() {
        return this.launchDamageContext;
    }

    public double distance() {
        return this.distance;
    }
}
