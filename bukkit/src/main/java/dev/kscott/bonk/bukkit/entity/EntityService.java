package dev.kscott.bonk.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class EntityService {

    /**
     * Launches entities away from the provided Location.
     * <p>
     * If {@code dropoff} is true, then the farther away an entity is from {@code location}, the lesser knockback they'll take.
     * @param location
     * @param radius
     * @param power
     * @param dropoff
     * @return
     */
    public @NonNull Map<Entity, Double> launchEntities(
            final @NonNull Location location,
            final int radius,
            final int power,
            final boolean dropoff
    ) {
        final @NonNull Collection<LivingEntity> entities = location.getNearbyLivingEntities(radius);

        final @NonNull Map<Entity, Double> knockbackMap = new HashMap<>();

        for (final @NonNull LivingEntity livingEntity : entities) {
            final @NonNull Location entityLocation = livingEntity.getLocation();

            final double distance = location.distanceSquared(entityLocation);

            // TODO: better dropoff calculation
            final double knockback = dropoff ? power - distance : power;

            final @NonNull Vector knockbackVector = entityLocation.toVector().subtract(location.toVector());

            knockbackVector.setY(knockback);

            livingEntity.setVelocity(knockbackVector);

            knockbackMap.put(livingEntity, knockback);
        }

        return knockbackMap;

    }

}
