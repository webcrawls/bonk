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
    @NonNull String getId();

    /**
     * {@return the name of this weapon}
     */
    @NonNull Component getName();

    /**
     * {@return the description of this weapon}
     */
    @NonNull Component[] getDescription();

    /**
     * {@return the strength of this weapon}
     */
    double getStrength();

    /**
     * {@return the damage of this weapon}
     */
    double getDamage();

}
