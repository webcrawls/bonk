package dev.kscott.bonk.bukkit.minigame;

/**
 * Represents a Minigame.
 */
public interface Minigame {

    /**
     * Enables this minigame.
     */
    void enable();

    /**
     * Disables this minigame.
     */
    void disable();

    /**
     * {@return true if this minigame is enabled; false if disabled}
     */
    boolean enabled();

}
