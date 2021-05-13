package dev.kscott.bonk.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides methods for interacting with entities.
 */
public final class EntityService {

    /**
     * Launches entities away from the provided Location.
     * <p>
     * If {@code dropoff} is true, then the farther away an entity is from {@code location}, the lesser knockback they'll take.
     *
     * @param location the location of the launch point
     * @param radius   the radius of the launch
     * @param power    the power of the launch
     * @param dropoff  should power dropoff be enabled
     * @return a map containing all the entities affected by this launch, and how much knockback they received
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

            @NonNull Vector knockbackVector = entityLocation.toVector().add(location.toVector().multiply(-1));

            knockbackVector.add(new Vector(0, knockback, 0));

            livingEntity.setVelocity(knockbackVector);

            knockbackMap.put(livingEntity, knockback);
        }

        return knockbackMap;

    }

}
