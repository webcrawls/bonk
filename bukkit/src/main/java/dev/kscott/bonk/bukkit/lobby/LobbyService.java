package dev.kscott.bonk.bukkit.lobby;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import dev.kscott.bonk.bukkit.player.BonkSpirit;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Handles Bonk lobby stuff.
 */
@Singleton
public class LobbyService {

    private static final @NonNull MiniMessage miniMessage = MiniMessage.get();

    private final @NonNull Set<BonkSpirit> members; // set of uuids containing all players in lobby
    private final @NonNull Location lobbyLocation; // the base lobby location
    private final @NonNull World gameWorld; // the game world
    private final @NonNull Random random;
    private final @NonNull JavaPlugin plugin;
    private final @NonNull BukkitRunnable lobbyRunnable;
    private final @NonNull BossBar bossBar;
    private final @NonNull Title welcomeTitle;

    private float messagePhase;

    /**
     * Constructs {@code LobbyService}.
     *
     * @param gameWorld the game world
     * @param plugin    the plugin
     */
    @Inject
    public LobbyService(final @NonNull @Named("gameWorld") World gameWorld,
                        final @NonNull BukkitBonkPlugin plugin) {
        this.gameWorld = gameWorld;
        this.plugin = plugin;
        this.messagePhase = 0;

        this.bossBar = BossBar.bossBar(
                miniMessage.parse("<" + Colours.GRAY_LIGHT.asHexString() + ">Run <bold><gradient:#e94b41:#e99f41:" + messagePhase + ">/bonk</gradient></bold> to play!</" + Colours.GRAY_LIGHT.asHexString() + ">"),
                1,
                BossBar.Color.BLUE,
                BossBar.Overlay.NOTCHED_6
        );

        this.members = new HashSet<>();
        this.random = new Random();
        this.lobbyLocation = new Location(this.gameWorld, -79.5, 195, -54.5, -180, 0);

        this.lobbyRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (messagePhase > 1) {
                    messagePhase = -1;
                }

                if (messagePhase < -1) {
                    messagePhase = 1;
                }

                bossBar.name(miniMessage.parse("<" + Colours.GRAY_LIGHT.asHexString() + ">Run <bold><gradient:#e94b41:#e99f41:" + messagePhase + ">/bonk</gradient></bold> to play!</" + Colours.GRAY_LIGHT.asHexString() + ">"));

                messagePhase -= 0.05;
            }
        };

        this.lobbyRunnable.runTaskTimer(this.plugin, 0, 0);

        this.welcomeTitle = Title.title(
                Component.text()
                        .append(Component.text("Welcome to "))
                        .append(Component.text("Bonk")
                                .decoration(TextDecoration.BOLD, true)
                                .color(Colours.YELLOW)
                        )
                        .append(Component.text("!"))
                        .style(Styles.TEXT)
                        .build(),
                Component.empty()
        );
    }

    /**
     * Handles a player joining the lobby.
     *
     * @param spirit the player
     */
    public void add(final @NonNull BonkSpirit spirit) {
        final @NonNull Player player = spirit.player();

        player.setGameMode(GameMode.SPECTATOR);
        player.showBossBar(bossBar);
        player.teleportAsync(lobbyLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.setFallDistance(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                members.add(spirit);

                player.showTitle(welcomeTitle);
            }
        }.runTaskLater(this.plugin, 20);
    }

    /**
     * Handles players leaving the game.
     */
    public void remove(final @NonNull BonkSpirit spirit) {
        this.members.remove(spirit);
        spirit.player().hideBossBar(this.bossBar);
    }

    /**
     * Returns true if the player is in the lobby, false if the player isn't.
     *
     * @param player the player
     * @return true if in lobby, false if not
     */
    public boolean inLobby(final @NonNull Player player) {
        for (final @NonNull BonkSpirit spirit : this.members) {
            if (spirit.player().getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

}
