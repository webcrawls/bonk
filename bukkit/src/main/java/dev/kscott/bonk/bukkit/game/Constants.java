package dev.kscott.bonk.bukkit.game;

import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Stores constants for reuse across other classes.
 */
public class Constants {

    /**
     * Potions.
     */
    public static class Potions {

        /**
         * The jump boost potion effect.
         */
        public static final @NonNull PotionEffect JUMP_BOOST = new PotionEffect(
                PotionEffectType.JUMP,
                9999999,
                2,
                true,
                true,
                false
        );

    }

    /**
     * {@link NamespacedKey}s.
     */
    public static class Keys {

        public static final @NonNull NamespacedKey ITEM_WEAPON_KEY = new NamespacedKey("bonk", "weapon");

    }

}
