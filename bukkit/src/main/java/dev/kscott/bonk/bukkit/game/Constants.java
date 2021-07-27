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

        public static final @NonNull PotionEffect GLOWING = new PotionEffect(
                PotionEffectType.GLOWING,
                9999999,
                1,
                true,
                true,
                false
        );

        public static final @NonNull PotionEffect FEATHER = new PotionEffect(
                PotionEffectType.SLOW_FALLING,
                100,
                1,
                true,
                true,
                false
        );

        public static final @NonNull PotionEffect LEVITATION = new PotionEffect(
                PotionEffectType.LEVITATION,
                100,
                1,
                true,
                true,
                false
        );

    }

    /**
     * {@link NamespacedKey}s.
     */
    public static class Keys {

        /**
         * The {@link NamespacedKey} to be used for bonk weapon ids.
         */
        public static final @NonNull NamespacedKey ITEM_WEAPON_KEY = new NamespacedKey("bonk", "weapon");

    }

    /**
     * Magic numbers.
     */
    public static class Numbers {

        /**
         * Minecraft's gravitational constant.
         */
        public static final double GRAVITY = -0.0784000015258789;

    }

}
