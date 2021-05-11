package dev.kscott.bonk.bukkit.command;

import cloud.commandframework.CommandManager;
import dev.kscott.bonk.api.player.BonkPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The base command, extended by all Bonk commands.
 */
public abstract class BaseCommand {

    /**
     * Registers this command with {@code commandManager}.
     * @param commandManager the Bonk command manager
     */
    public abstract void register(final @NonNull CommandManager<BonkPlayer> commandManager);

}
