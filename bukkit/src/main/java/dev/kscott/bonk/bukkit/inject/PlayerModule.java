package dev.kscott.bonk.bukkit.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.kscott.bonk.bukkit.player.PlayerService;

/**
 * Provides the PlayerService.
 */
public final class PlayerModule extends AbstractModule {

    /**
     * Binds the PlayerService class to a singleton scope.
     */
    @Override
    public void configure() {
        this.bind(PlayerService.class).in(Scopes.SINGLETON);
    }

}
