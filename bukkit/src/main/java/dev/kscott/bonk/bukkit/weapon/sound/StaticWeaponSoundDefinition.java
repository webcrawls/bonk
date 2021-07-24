package dev.kscott.bonk.bukkit.weapon.sound;

import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;

public record StaticWeaponSoundDefinition(
        @NonNull Sound sound,
        float pitch,
        float volume
) implements WeaponSoundDefinition {
}
