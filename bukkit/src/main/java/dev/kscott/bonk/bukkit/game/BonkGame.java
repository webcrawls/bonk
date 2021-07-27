package dev.kscott.bonk.bukkit.game;

import com.google.inject.Inject;
import com.google.inject.Injector;
import dev.kscott.bonk.bukkit.box.BoxService;
import dev.kscott.bonk.bukkit.log.LoggingService;
import dev.kscott.bonk.bukkit.minigame.CreeperMinigame;
import dev.kscott.bonk.bukkit.minigame.Minigame;
import dev.kscott.bonk.bukkit.player.DoubleJumpService;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import dev.kscott.bonk.bukkit.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Manages the Bonk game.
 */
public final class BonkGame {

    /**
     * The array of minigames to enable.
     */
    private static final @NonNull Class<? extends Minigame>[] MINIGAMES = ArrayHelper.create(
            CreeperMinigame.class
    );

    private final @NonNull JavaPlugin plugin;
    private final @NonNull LoggingService loggingService;
    private final @NonNull DoubleJumpService doubleJumpService;
    private final @NonNull List<Minigame> enabledMinigames;
    private final @NonNull BoxService boxService;
    private final @NonNull Injector injector;

    /**
     * Constructs {@code BonkGame}.
     *
     * @param injector          the parent injector
     * @param plugin            the plugin
     * @param loggingService    the logging service
     * @param doubleJumpService the double jump service
     */
    @Inject
    public BonkGame(
            final @NonNull Injector injector,
            final @NonNull JavaPlugin plugin,
            final @NonNull LoggingService loggingService,
            final @NonNull BoxService boxService,
            final @NonNull DoubleJumpService doubleJumpService
    ) {
        this.injector = injector;
        this.plugin = plugin;
        this.loggingService = loggingService;
        this.doubleJumpService = doubleJumpService;
        this.boxService = boxService;
        this.enabledMinigames = new ArrayList<>();
    }

    /**
     * Enables the Bonk game.
     */
    public void enable() {
        // load minigames
        for (final @NonNull Class<? extends Minigame> klazz : MINIGAMES) {
            final @NonNull Minigame minigame = this.injector.getInstance(klazz);
            this.loggingService.debug("Enabled minigame " + minigame.getClass().getSimpleName());
            this.enabledMinigames.add(minigame);
        }

        // start game tick
        new BukkitRunnable() {
            @Override
            public void run() {
                tick();
            }
        }.runTaskTimer(this.plugin, 0, 1);

        this.boxService.init();

        this.loggingService.debug("Started game tick");
    }

    /**
     * Runs the game tick.
     */
    private void tick() {
        // Double jump logic
        {
            final @NonNull List<Player> allowed = new ArrayList<>();

            for (final @NonNull UUID uuid : this.doubleJumpService.players()) {
                final @Nullable Player player = Bukkit.getPlayer(uuid);

                if (player == null) {
                    continue;
                }


                if (PlayerUtils.isNearGround(player)) {
                    allowed.add(player);
                }
            }

            // Prevent CME
            for (final @NonNull Player player : allowed) {
                this.doubleJumpService.canDoubleJump(player, true);
            }
        }
    }

}
