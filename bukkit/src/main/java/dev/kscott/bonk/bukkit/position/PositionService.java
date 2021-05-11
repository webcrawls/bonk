package dev.kscott.bonk.bukkit.position;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Provides {@link GamePosition}s.
 */
public class PositionService {

    /**
     * The lobby spawn position.
     */
    private final @NonNull GamePosition lobbySpawnPosition;

    /**
     * Collection of positions to spawn players at.
     */
    private final @NonNull Collection<@NonNull GamePosition> gameSpawnPositions;

    /**
     * Collection of positions to spawn boxes at.
     */
    private final @NonNull Collection<@NonNull GamePosition> boxPositions;

    /**
     * Constructs {@code PositionService}.
     */
    public PositionService() {
        this.lobbySpawnPosition = new GamePosition(-69.5, 117, -456.5, 0, 0);
        this.boxPositions = List.of();
        this.gameSpawnPositions = List.of();
    }

    /**
     * {@return the position of the lobby spawn}
     */
    public @NonNull GamePosition lobbyPosition() {
        return lobbySpawnPosition;
    }

    /**
     * {@return the collection of game spawnpoints}
     */
    public @NonNull Collection<@NonNull GamePosition> spawnPositions() {
        return gameSpawnPositions;
    }

    /**
     * {@return the collection of box spawnpoints}
     */
    public @NonNull Collection<@NonNull GamePosition> boxPositions() {
        return boxPositions;
    }
}
