package dev.kscott.bonk.bukkit.player;

/**
 * Represents a Bonk player's playing state.
 */
public enum PlayerState {
    /**
     * Used when the player is in game (i.e. actively playing Bonk).
     */
    IN_GAME,
    /**
     * Used when the player is registered with Bonk, but not playing (i.e. in the lobby)
     */
    PRE_GAME
}
