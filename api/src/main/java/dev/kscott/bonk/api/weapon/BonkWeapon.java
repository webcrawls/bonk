package dev.kscott.bonk.api.weapon;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The {@code BonkWeapon} interface.
 */
public interface BonkWeapon {

    /**
     * {@return the id of this weapon}
     */
    @NonNull String id();

    /**
     * {@return the name of this weapon}
     */
    @NonNull Component name();

    /**
     * {@return the description of this weapon}
     */
    @NonNull Component[] description();

    /**
     * {@return the strength of this weapon}
     */
    double strength();

    /**
     * {@return the damage of this weapon}
     */
    double damage();

}
