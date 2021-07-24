package dev.kscott.bonk.bukkit.weapon.sound;

import dev.kscott.bonk.bukkit.weapon.Weapon;
import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a sound that a {@link Weapon} will make when used.
 */
public interface WeaponSoundDefinition {

    static @NonNull WeaponSoundDefinition of(final @NonNull Sound sound) {
        return new StaticWeaponSoundDefinition(sound, 1, 1);
    }

    static @NonNull WeaponSoundDefinition of(final @NonNull Sound sound,
                                             final float pitch) {
        return new StaticWeaponSoundDefinition(sound, pitch, 1);
    }

    static @NonNull WeaponSoundDefinition of(final @NonNull Sound sound,
                                             final float pitch,
                                             final float volume) {
        return new StaticWeaponSoundDefinition(sound, pitch, volume);
    }

    static @NonNull WeaponSoundDefinition random(final @NonNull Sound sound,
                                                 final float volume,
                                                 final float pitchMin,
                                                 final float pitchMax) {
        return new RandomWeaponSoundDefinition(sound, volume, pitchMin, pitchMax);
    }

    float pitch();

    float volume();

    @NonNull Sound sound();

}
