package dev.kscott.bonk.bukkit.minigame;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.position.PositionService;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The creeper minigame.
 */
public class CreeperMinigame implements Minigame {

    private final @NonNull PositionService positionService;

    @Inject
    public CreeperMinigame(
            final @NonNull PositionService positionService
    ) {
        this.positionService = positionService;
    }

    /**
     * If true, game is enabled. If false, is disabled.
     */
    private final boolean enabled = false;

    @Override
    public void enable() {
    }

    @Override
    public void disable() {

    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }
}
