package dev.kscott.bonk.bukkit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.bonk.bukkit.command.CommandService;
import dev.kscott.bonk.bukkit.game.BonkGame;
import dev.kscott.bonk.bukkit.inject.BukkitModule;
import dev.kscott.bonk.bukkit.inject.CommandModule;
import dev.kscott.bonk.bukkit.inject.GameModule;
import dev.kscott.bonk.bukkit.listeners.*;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The main Bonk entrypoint.
 */
public final class BukkitBonkPlugin extends JavaPlugin {

    /**
     * An array of {@link Listener} classes to initialize during startup.
     */
    private static final @NonNull Class<? extends Listener>[] LISTENERS = ArrayHelper.create(
            PlayerConnectionListeners.class,
            BlockInteractListener.class,
            PlayerMovementListeners.class,
            PlayerDeathListeners.class,
            PlayerAttributeListeners.class
    );

    /**
     * The Guice injector.
     */
    private @NonNull Injector injector;

    /**
     * The {@code BonkGame} instance.
     */
    private @MonotonicNonNull BonkGame bonkGame;

    /**
     * Constructs {@code BukkitBonkPlugin}.
     */
    public BukkitBonkPlugin() {
        // Initialize Guice shenanigans
        this.injector = Guice.createInjector(
                new BukkitModule(this)
        );
    }

    @Override
    public void onEnable() {
        this.injector = this.injector.createChildInjector(
                new CommandModule(this),
                new GameModule(this)
        );

        this.bonkGame = injector.getInstance(BonkGame.class);

        this.injector.getInstance(CommandService.class); // Initialize command service

        // Register events
        for (final @NonNull Class<? extends Listener> klazz : LISTENERS) {
            this.getServer().getPluginManager().registerEvents(
                    this.injector.getInstance(klazz),
                    this
            );
        }

        this.bonkGame.enable();
    }

}
