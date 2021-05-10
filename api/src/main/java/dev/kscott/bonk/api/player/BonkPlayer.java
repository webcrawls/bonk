package dev.kscott.bonk.api.player;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.checkerframework.checker.nullness.qual.NonNull;
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


}
