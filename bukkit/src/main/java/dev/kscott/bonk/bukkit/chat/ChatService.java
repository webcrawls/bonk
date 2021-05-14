package dev.kscott.bonk.bukkit.chat;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.player.BonkPlayer;
import dev.kscott.bonk.bukkit.player.PlayerService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Handles chat stuff.
 */
public final class ChatService {

    /**
     * The player service.
     */
    private final @NonNull PlayerService playerService;

    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Constructs {@code ChatService}.
     *
     * @param playerService the player service
     * @param plugin        the plugin
     */
    @Inject
    public ChatService(
            final @NonNull PlayerService playerService,
            final @NonNull JavaPlugin plugin
    ) {
        this.plugin = plugin;
        this.playerService = playerService;
    }

    /**
     * Handles the {@link AsyncChatEvent}.
     *
     * @param event chat event
     */
    public void chat(final @NonNull AsyncChatEvent event) {
        final @NonNull Component text = MiniMessage.get()
                .parse("<gradient:#72e5ed:#d4f7fa><bold>" + event.getPlayer().getName() + "</bold></gradient> <gray>\\></gray>")
                .append(event.message().style(Style.style(TextColor.color(192, 205, 207))));


        new BukkitRunnable() {
            @Override
            public void run() {
                final @NonNull Collection<BonkPlayer> players = playerService.players();

                for (final @NonNull BonkPlayer recipient : players) {
                    recipient.player().sendMessage(text);
                }
            }
        }.runTask(this.plugin);
    }

}
