package dev.kscott.bonk.bukkit.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void playerDamage(final @NonNull EntityDamageEvent event) {
        final @NonNull Entity entity = event.getEntity();

        if (entity instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                // Jump boost absorbs a lot of damage
                event.setDamage(event.getDamage() * 1.35);
            }
        }
    }

}
