package dev.kscott.bonk.bukkit.weapon.sound;

import dev.kscott.bonk.bukkit.weapon.Weapon;
import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * A weapon sound with a random pitch.
 */
public class RandomWeaponSoundDefinition implements WeaponSoundDefinition {

    private final @NonNull Sound sound;
    private final @NonNull Random random;
    private final float volume;
    private final float pitchMin;
    private final float pitchMax;

    /**
     * Represents a sound that a {@link Weapon} will make when used.
     *
     * @param sound  the sound to play
     * @param volume the float of the sound
     */
    public RandomWeaponSoundDefinition(@NonNull Sound sound,
                                       float volume,
                                       final float pitchMin,
                                       final float pitchMax) {
        this.volume = volume;
        this.pitchMax = pitchMax;
        this.pitchMin = pitchMin;
        this.sound = sound;

        this.random = new Random();
    }

    @Override
    public float pitch() {
        return this.pitchMin + this.random.nextFloat() * (this.pitchMax - this.pitchMin);
    }

    @Override
    public float volume() {
        return this.volume;
    }

    @Override
    public @NotNull Sound sound() {
        return this.sound;
    }
}
