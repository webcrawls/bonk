package dev.kscott.bonk.bukkit.inject;

import com.google.inject.AbstractModule;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides Bukkit-related classes.
 */
public class BukkitModule extends AbstractModule {

    /**
     * The {@link BukkitBonkPlugin} to provide.
     */
    private final @NonNull BukkitBonkPlugin plugin;

    /**
     * Constructs {@code BukkitModule}.
     *
     * @param plugin the plugin
     */
    public BukkitModule(final @NonNull BukkitBonkPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Binds plugin and audience classes.
     */
    @Override
    public void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(JavaPlugin.class).toInstance(this.plugin);
        this.bind(BukkitBonkPlugin.class).toInstance(this.plugin);
    }
}
