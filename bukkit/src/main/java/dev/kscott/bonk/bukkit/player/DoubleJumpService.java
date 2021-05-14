package dev.kscott.bonk.bukkit.player;

import com.google.inject.Inject;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Handles double jumping.
 */
public final class DoubleJumpService {

    /**
     * A set of {@code UUID} where the UUID is a Player who has double-jumped.
     */
    private final @NonNull Set<UUID> doubleJumpedPlayers;

    /**
     * Constructs {@code DoubleJumpService}.
     */
    @Inject
    public DoubleJumpService() {
        this.doubleJumpedPlayers = new HashSet<>();
    }

    /**
     * Triggers a double jump and prevents them from doing so again (until they land)
     *
     * @param player Player
     */
    public void doubleJump(final @NonNull Player player) {
        player.setVelocity(player.getLocation().getDirection().add(new Vector(0, 1, 0).multiply(1.1)));
        player.setFallDistance(0);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 60);
        this.canDoubleJump(player, false);
    }

    /**
     * Controls whether or not the player can double jump.
     *
     * @param player player
     * @param can    boolean
     */
    private void canDoubleJump(final @NonNull Player player, final boolean can) {
        if (can) {
            this.doubleJumpedPlayers.remove(player.getUniqueId());
        } else {
            this.doubleJumpedPlayers.add(player.getUniqueId());
        }
    }

    /**
     * Returns whether or not the player can double jump.
     *
     * @param player player
     * @return boolean
     */
    public boolean canDoubleJump(final @NonNull Player player) {
        return !this.doubleJumpedPlayers.contains(player.getUniqueId());
    }

    /**
     * Returns a set containing all UUIDs of players who are double jumping.
     */
    public @NonNull Set<UUID> players() {
        return Collections.unmodifiableSet(this.doubleJumpedPlayers);
    }

}
