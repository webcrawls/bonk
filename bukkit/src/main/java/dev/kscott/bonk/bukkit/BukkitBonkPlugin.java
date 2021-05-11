package dev.kscott.bonk.bukkit;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.bonk.bukkit.game.BonkGame;
import dev.kscott.bonk.bukkit.inject.BukkitModule;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The main Bonk entrypoint.
 */
public final class BukkitBonkPlugin extends JavaPlugin {

    /**
     * An array of {@link Listener} classes to initialize during startup.
     */
    private static final Class<? extends Listener>[] LISTENERS = ArrayHelper.create(

    );

    /**
     * The Guice injector.
     */
    private @NonNull Injector injector;

    /**
     * The {@code BonkGame} instance.
     */
    private final @NonNull BonkGame bonkGame;

    /**
     * Constructs {@code BukkitBonkPlugin}.
     */
    public BukkitBonkPlugin() {
        this.injector = Guice.createInjector(
                new BukkitModule(this)
        );

        this.bonkGame = injector.getInstance(BonkGame.class);
        this.injector = bonkGame.load();
    }

}
