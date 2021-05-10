package dev.kscott.bonk.bukkit.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on {@link PlayerJoinEvent}.
 */
public class PlayerJoinListener implements Listener {

    /**
     * Sends a welcome message to the player when they join.
     *
     * @param event {@link PlayerJoinEvent}
     */
    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {
        final @NonNull Player player = event.getPlayer();

        player.sendMessage(Component.text("Welcome to Bonk, "+player.getName()+"!"));
    }

    /**
     * Blesses a player with the power of Scuttle.
     *
     * @param event {@link PlayerJoinEvent}
     */
    public void blessWithScuttle(final @NonNull PlayerJoinEvent event) {
        final @NonNull Player player = event.getPlayer();
        Parrot parrot = (Parrot) player.getWorld().spawnEntity(player.getLocation(), EntityType.PARROT);
        parrot.setCustomNameVisible(true);
        parrot.customName();
    }

}
