package dev.kscott.bonk.bukkit.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides adventure audiences.
 */
public final class AudienceModule extends AbstractModule {

    /**
     * Provides {@link BukkitAudiences}.
     *
     * @param plugin the plugin to create the audience with
     * @return audience
     */
    @Inject
    @Singleton
    @Provides
    public @NonNull BukkitAudiences audiences(final @NonNull BukkitBonkPlugin plugin) {
        return BukkitAudiences.create(plugin);
    }

}
