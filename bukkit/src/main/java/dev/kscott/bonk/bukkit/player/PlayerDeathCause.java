package dev.kscott.bonk.bukkit.player;

/**
 * Represents a Bonk player's death cause.
 */
public enum PlayerDeathCause {

    /**
     * When a player is killed by another player.
     */
    PLAYER,
    /**
     * When a player is killed by fall damage.
     */
    FALL,
    /**
     * When a player is killed by falling into the void.
     */
    VOID,
    /**
     * When the death cause couldn't be determined/isn't important.
     */
    UNKNOWN

}
