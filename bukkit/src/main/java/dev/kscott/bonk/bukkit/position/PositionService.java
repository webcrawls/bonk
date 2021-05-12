package dev.kscott.bonk.bukkit.position;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Provides {@link GamePosition}s.
 */
public final class PositionService {

    /**
     * The world of the game.
     */
    private final @NonNull World world;

    /**
     * The Random instance.
     */
    private final @NonNull Random random;

    /**
     * The lobby spawn position.
     */
    private final @NonNull GamePosition lobbySpawnPosition;

    /**
     * Collection of positions to spawn players at.
     */
    private final @NonNull List<@NonNull GamePosition> gameSpawnPositions;

    /**
     * Collection of positions to spawn boxes at.
     */
    private final @NonNull List<@NonNull GamePosition> boxPositions;

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

        this.gameSpawnPositions = List.of(
                new GamePosition(-79.5, 105, -91.5, 0, 0),
                new GamePosition(-79.5, 105, -91.5, -90, 0),
                new GamePosition(-79.5, 105, -91.5, 180, 0),
                new GamePosition(-79.5, 105, -91.5, 90, 0),
                new GamePosition(-67.5, 97, -79.5, 90, 0),
                new GamePosition(-67.5, 97, -79.5, 180, 0),
                new GamePosition(-91.5, 97, -79.5, -90, 0),
                new GamePosition(-91.5, 97, -79.5, 180, 0),
                new GamePosition(-79.5, 94, -77, 0, 0),
                new GamePosition(-79.5, 94, -77, -90, 0),
                new GamePosition(-79.5, 94, -77, 180, 0),
                new GamePosition(-79.5, 94, -77, 90, 0)
        );

        this.random = new Random();
    }

    /**
     * {@return the position of the lobby spawn}
     */
    public @NonNull GamePosition lobbyPosition() {
        return this.lobbySpawnPosition;
    }

    /**
     * {@return the collection of game spawn points}
     */
    public @NonNull Collection<@NonNull GamePosition> spawnPositions() {
        return this.gameSpawnPositions;
    }

    /**
     * {@return a random spawn position}
     */
    public @NonNull GamePosition spawnPosition() {
        final int index = this.random.nextInt(this.gameSpawnPositions.size());

        return this.gameSpawnPositions.get(index);
    }

    /**
     * {@return the collection of box spawn points}
     */
    public @NonNull Collection<@NonNull GamePosition> boxPositions() {
        return this.boxPositions;
    }

    /**
     * {@return a random box position}
     */
    public @NonNull GamePosition boxPosition() {
        final int index = this.random.nextInt(this.boxPositions.size());

        return this.boxPositions.get(index);
    }

    /**
     * {@return the game's world}
     */
    public @NonNull World world() {
        return this.world;
    }
}
