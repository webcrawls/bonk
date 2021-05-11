package dev.kscott.bonk.bukkit.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.bonk.bukkit.player.PlayerService;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PlayerModule extends AbstractModule {

    private final @NonNull PlayerService service;

    public PlayerModule() {
        this.service = new PlayerService();
    }

    @Singleton
    @Provides
    public @NonNull PlayerService playerService() {
        return this.service;
    }

}
