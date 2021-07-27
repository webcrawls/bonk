package dev.kscott.bonk.bukkit.listeners;

import dev.kscott.bonk.bukkit.player.BonkSpirit;
import dev.kscott.bonk.bukkit.player.PlayerService;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireworkListeners implements Listener {

    private final @NonNull PlayerService playerService;

    @Inject
    public FireworkListeners(final @NonNull PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onFireworkHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof final Firework firework)) {
            System.out.println("returned, not firework");
            return;
        }

        if (!firework.getFireworkMeta().getPersistentDataContainer().has(BonkSpirit.AMMO_DATA_KEY, PersistentDataType.STRING));

        final @NonNull Location fireworkLocation = event.getEntity().getLocation();

        Player shooter = null;

        if (event.getEntity().getShooter() instanceof Player) {
            shooter = (Player) event.getEntity().getShooter();
        }

        final @NonNull Map<LivingEntity, Double> damageMap = defaultExplode(fireworkLocation, shooter);

        for (var entry : damageMap.entrySet()) {
            // Attribute damage to shooter
            if (entry.getKey() instanceof Player) {
                final @NonNull EntityDamageEvent event2 =  entry.getKey().getLastDamageCause();

                if (event2 != null) {
                    this.playerService.handlePlayerDamage(event2);
                }
            }
        }
    }

    /**
     * Executes the default explosion code.
     *
     * @param location Location of the explosion
     * @param shooter  Player who shot
     * @return A map where the key is a player, and the value is how much damage was dealt to that player
     */
    protected Map<LivingEntity, Double> defaultExplode(
            final @NonNull Location location,
            final @NonNull Player shooter
    ) {
        System.out.println("default explode");

        return PlayerService.propelEntitiesAt(location, 4, 2, false);

//        // Iterate all nearby living entities.
//        for (final @NonNull LivingEntity livingEntity : location.getNearbyLivingEntities(3, 3, 3)) {
//
//            System.out.println(livingEntity.getName());
//
//            // Grab LE's location.
//            final @NonNull Location playerLocation = livingEntity.getLocation();
//
//            // Calculate & apply damage
//            final double dist = location.distanceSquared(playerLocation);
//            final double damage = 4 - dist * 0.5;
//            livingEntity.damage(damage, shooter);
//
//            // Apply knockback force
//            final @NonNull Vector direction = playerLocation.toVector().subtract(location.toVector());
//            direction.setY(0.4);
//            livingEntity.setVelocity(direction);
//
//            // If player, add damage to damage map.
//            if (livingEntity instanceof Player && damage > 0) {
//                map.put((Player) livingEntity, damage);
//            }
//
//            System.out.println(livingEntity);
//        }
//
//        return map;
    }

}
