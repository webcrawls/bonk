package dev.kscott.bonk.bukkit.game;

import com.google.inject.Inject;
import com.google.inject.Injector;
import dev.kscott.bonk.bukkit.command.CommandService;
import dev.kscott.bonk.bukkit.inject.CommandModule;
import dev.kscott.bonk.bukkit.inject.GameModule;
import dev.kscott.bonk.bukkit.log.LoggingService;
import dev.kscott.bonk.bukkit.minigame.CreeperMinigame;
import dev.kscott.bonk.bukkit.minigame.Minigame;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * The injector provided by the parent class
     */
    private final @NonNull Injector parentInjector;
    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;
    /**
     * The pogging service.
     */
    private final @NonNull LoggingService loggingService;
    /**
     * A list of enabled minigames.
     */
    private final @NonNull List<Minigame> enabledMinigames;

    /**
     * The injector used by {@code BonkGame}.
     */
    private @MonotonicNonNull Injector injector;


    /**
     * Constructs {@code BonkGame}.
     *
     * @param parentInjector the parent injector
     * @param plugin         the plugin
     * @param loggingService         the logging service
     */
    @Inject
    public BonkGame(
            final @NonNull Injector parentInjector,
            final @NonNull JavaPlugin plugin,
            final @NonNull LoggingService loggingService
    ) {
        this.parentInjector = parentInjector;
        this.plugin = plugin;
        this.loggingService = loggingService;
        this.enabledMinigames = new ArrayList<>();
    }

    /**
     * Loads {@code BonkGame}'s dependencies.
     * <p>
     * The injector is created using the injector passed into the constructor.
     *
     * @return {@code BonkGame}'s dependencies
     */
    public @NonNull Injector load() {
        this.injector = this.parentInjector.createChildInjector(
                new CommandModule(this.plugin),
                new GameModule(this.plugin)
        );

        this.injector.getInstance(CommandService.class);

        return this.injector;
    }

    /**
     * Enables the Bonk game.
     */
    public void enable() {
        // load minigames
        for (final @NonNull Class<? extends Minigame> klazz : MINIGAMES) {
            final @NonNull Minigame minigame = this.injector.getInstance(klazz);
            this.loggingService.debug("Enabled minigame "+minigame.getClass().getSimpleName());
            this.enabledMinigames.add(minigame);
        }
    }

    private void tick() {

    }

}
