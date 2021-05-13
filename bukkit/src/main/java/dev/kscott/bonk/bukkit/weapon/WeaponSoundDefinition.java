package dev.kscott.bonk.bukkit.weapon;

import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a sound that a {@link Weapon} will make when used.
 *
 * @param sound  the sound to play
 * @param volume the float of the sound
 * @param pitch  the pitch of the sound
 */
public record WeaponSoundDefinition(
        @NonNull Sound sound,
        float volume,
        float pitch
) {
}
