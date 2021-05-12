package dev.kscott.bonk.bukkit.player;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.position.PositionService;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages the players of Bonk.
 */
public final class PlayerService {

    /**
     * An array of components to send to players upon joining Bonk.
     */
    private static final Component[] MOTD_COMPONENTS = ArrayHelper.create(
            MiniMessage.get().parse("<gradient:#b01e13:#de5a50:#b01e13><st>                                        </st></gradient>"),
            MiniMessage.get().parse("<gray>Welcome to <color:#de5a50>Bonk!</color:#de5a50></gray>"),
            MiniMessage.get().parse("<gray>Developed by bluely, with lots of help from Bing.</gray>"),
            MiniMessage.get().parse("<gray>Check out the code on <dark_aqua>GitHub!</dark_aqua></gray>"),
            Component.empty(),
            MiniMessage.get().parse("<gray>To start playing, run <aqua>/play</aqua>."),
            MiniMessage.get().parse("<gradient:#b01e13:#de5a50:#b01e13><st>                                        </st></gradient>")
    );

    /**
     * Controls whether or not the MOTD should be sent to the player.
     */
    private static final boolean SEND_MOTD = true;

    /**
     * The PositionService dependency.
     */
    private final @NonNull PositionService positionService;

    /**
     * Stores all online Bonk players.
     */
    private final @NonNull Set<BonkPlayer> players;

    /**
     * Constructs {@code PlayerService}.
     */
    @Inject
    public PlayerService(
            final @NonNull PositionService positionService
    ) {
        this.positionService = positionService;
        this.players = new HashSet<>();
    }

    /**
     * Returns the {@link BonkPlayer} associated with {@code player}.
     * <p>
     * If there is no {@link BonkPlayer} associated with {@code player},
     * and {@code player} is online, then a new {@link BonkPlayer} will
     * be created and {@code player} will be initialized into the game.
     *
     * @param player player
     * @return the {@link BonkPlayer}
     */
    public @NonNull BonkPlayer joined(final @NonNull Player player) {
        if (!player.isOnline()) {
            throw new RuntimeException("Tried to create a player that is not online!");
        }

        boolean ingame = playing(player);

        if (ingame) {
            for (final @NonNull BonkPlayer bonkPlayer : players) {
                if (bonkPlayer.uuid().equals(player.getUniqueId())) {
                    return bonkPlayer;
                }
            }
        }

        final @NonNull BonkPlayer bonkPlayer = createNewBonkPlayer(player);

        return bonkPlayer;
    }

    /**
     * @param player
     */
    public void left(final @NonNull Player player) {
        players.removeIf(bonkPlayer -> bonkPlayer.uuid().equals(player.getUniqueId()));
    }

    /**
     * Returns {@code true} if {@code player} is playing Bonk; {@code false} if otherwise.
     *
     * @param player player
     * @return {@code true} if {@code player} is playing Bonk; {@code false} if otherwise
     */
    public boolean playing(final @NonNull Player player) {
        for (final @NonNull BonkPlayer bonkPlayer : players) {
            if (bonkPlayer.uuid().equals(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }


    /**
     * Creates (and initializes) a new {@link BonkPlayer} associated with {@code player}.
     *
     * @param player player
     * @return the {@link BonkPlayer}
     */
    private @NonNull BonkPlayer createNewBonkPlayer(final @NonNull Player player) {
        final @NonNull BonkPlayer bonkPlayer = new BonkPlayer(player);

        players.add(bonkPlayer);

        bonkPlayer.position(this.positionService.spawnPosition());

        if (SEND_MOTD) {
            for (final @NonNull Component component : MOTD_COMPONENTS) {
                player.sendMessage(component);
            }
        }

        return bonkPlayer;
    }
}
