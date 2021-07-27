package dev.kscott.bonk.bukkit.game.score;

import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class ScoringService {

    private final Map<UUID, Integer> deathsMap;
    private final Map<UUID, Integer> killsMap;
    private final Map<UUID, Integer> killstreakMap;

    public ScoringService() {
        this.deathsMap = new HashMap<>();
        this.killsMap = new HashMap<>();
        this.killstreakMap = new HashMap<>();
    }

    public int deaths(final UUID uuid) {
        return this.deathsMap.getOrDefault(uuid, 0);
    }

    public int kills(final UUID uuid) {
        return this.deathsMap.getOrDefault(uuid, 0);
    }

    public void addDeath(final UUID uuid) {
        this.deathsMap.put(uuid, deaths(uuid) + 1);
    }

    public void addKill(final UUID uuid) {
        this.killsMap.put(uuid, kills(uuid) + 1);
    }

}
