package dev.kscott.bonk.bukkit.game;

import com.google.inject.Inject;
import com.google.inject.Injector;
import dev.kscott.bonk.bukkit.inject.CommandModule;
import dev.kscott.bonk.bukkit.inject.GameModule;
import dev.kscott.bonk.bukkit.inject.PlayerModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Manages the Bonk game.
 */
public class BonkGame {

    /**
     * The injector provided by the parent class
     */
    private final @NonNull Injector parentInjector;

    /**
     * The injector used by {@code BonkGame}.
     */
    private @MonotonicNonNull Injector injector;

    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Constructs {@code BonkGame}.
     *
     * @param parentInjector the parent injector
     * @param plugin the plugin
     */
    @Inject
    public BonkGame(
            final @NonNull Injector parentInjector,
            final @NonNull JavaPlugin plugin
    ) {
        this.parentInjector = parentInjector;
        this.plugin = plugin;
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
                new PlayerModule(),
                new GameModule(this.plugin)
        );

        return this.injector;
    }

}
