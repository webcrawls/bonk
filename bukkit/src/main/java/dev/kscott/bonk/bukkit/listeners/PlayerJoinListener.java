package dev.kscott.bonk.bukkit.listeners;

import net.kyori.adventure.text.Component;
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
     * @param event {@link PlayerJoinEvent}.
     */
    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {
        final @NonNull Player player = event.getPlayer();

        player.sendMessage(Component.text("Welcome to Bonk, "+player.getName()+"!"));
    }

}
