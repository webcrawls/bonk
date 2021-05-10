package dev.kscott.bonk.api.player;

import dev.kscott.bonk.api.weapon.BonkWeapon;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

import java.util.UUID;

/**
 * The base BonkPlayer interface.
 */
public interface BonkPlayer extends Identity, Identified, ForwardingAudience.Single {

    /**
     * {@return the uuid}
     */
    @Pure
    @NonNull UUID uuid();

    /**
     * {@return the {@link Identity}}
     */
    default @NonNull Identity identity() {
        return this;
    }

    /**
     * {@return the username}
     */
    @Pure
    @NonNull String getUsername();

    /**
     * {@return the health}
     */
    double getHealth();

    /**
     * Returns if the player is playing or not.
     * <p>
     * If the player hasn't been spawned into the game field yet, this will
     * return {@code false}; otherwise {@code true}. This will return false
     * in situations such as when the player has just joined and has not selected
     * a weapon yet.
     *
     * @return if the player is playing or not.
     */
    boolean isPlaying();

    /**
     * Returns the current weapon of this player.
     * <p>
     * If there is no weapon selected (which may happen in certain cases),
     * this will return null. It is recommended you check if the player is playing
     * with {@link BonkPlayer#isPlaying} before getting their weapon.
     *
     * @return the current weapon
     */
    @Nullable BonkWeapon getCurrentWeapon();


}
