package dev.kscott.bonk.api.weapon;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The {@code BonkWeapon} interface.
 */
public interface BonkWeapon {

    @NonNull String getId();

    @NonNull Component getName();

    @NonNull Component[] getDescription();

}
