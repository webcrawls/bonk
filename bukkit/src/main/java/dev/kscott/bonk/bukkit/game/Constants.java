package dev.kscott.bonk.bukkit.game;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        public static PotionEffect JUMP_BOOST = new PotionEffect(
                PotionEffectType.JUMP,
                9999999,
                2,
                true,
                true,
                false
        );

    }

}
