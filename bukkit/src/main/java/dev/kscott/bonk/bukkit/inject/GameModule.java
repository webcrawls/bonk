package dev.kscott.bonk.bukkit.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Provides game-related objects.
 */
public final class GameModule extends AbstractModule {

    /**
     * The world of the game.
     */
    private final @NonNull World gameWorld;

    /**
     * Constructs {@code GameModule}.
     *
     * @param plugin the plugin
     */
    @Inject
    public GameModule(final @NonNull JavaPlugin plugin) {
        final @Nullable World world = plugin.getServer().getWorld("world");

        if (world == null) {
            throw new RuntimeException("The game world could not be found!");
        }

        this.gameWorld = world;
    }

    /**
     * {@return the game world}
     */
    @Provides
    @Named("gameWorld")
    public @NonNull World gameWorld() {
        return this.gameWorld;
    }

}
