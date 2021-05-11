package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on player join events.
 */
public final class PlayerJoinListener implements Listener {

    /**
     * An array of components to send to players on login.
     */
    private static final Component[] MOTD_COMPONENTS = ArrayHelper.create(
            MiniMessage.get().parse("<gradient:#b01e13:#de5a50:#b01e13><st>                                        </st></gradient>"),
            MiniMessage.get().parse("<gray>Welcome to <color:#de5a50>Bonk!</color:#de5a50></gray>"),
            MiniMessage.get().parse("<gray>Developed by kadenscott, with lots of help from Bing.</gray>"),
            MiniMessage.get().parse("<gray>Check out the source on <dark_aqua>GitHub!</dark_aqua></gray>"),
            MiniMessage.get().parse("<gradient:#b01e13:#de5a50:#b01e13><st>                                        </st></gradient>")
    );

    private final @NonNull PlayerService playerService;

    /**
     * Constructs PlayerJoinListener.
     *
     * @param playerService the PlayerService
     */
    @Inject
    public PlayerJoinListener(final @NonNull PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void handlePlayerJoin(final @NonNull PlayerJoinEvent event) {
        final @NonNull Player player = event.getPlayer();

        for (final @NonNull Component component : MOTD_COMPONENTS) {
            player.sendMessage(component);
        }
    }
}
