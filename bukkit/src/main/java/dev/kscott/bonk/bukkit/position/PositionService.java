package dev.kscott.bonk.bukkit.position;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Provides {@link GamePosition}s.
 */
public final class PositionService {

    /**
     * The world of the game.
     */
    private final @NonNull World world;

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
     *
     * @param world the game world
     */
    @Inject
    public PositionService(
            final @NonNull @Named("gameWorld") World world
    ) {
        this.world = world;
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
     * {@return the collection of game spawn points}
     */
    public @NonNull Collection<@NonNull GamePosition> spawnPositions() {
        return gameSpawnPositions;
    }

    /**
     * {@return the collection of box spawn points}
     */
    public @NonNull Collection<@NonNull GamePosition> boxPositions() {
        return boxPositions;
    }
}
