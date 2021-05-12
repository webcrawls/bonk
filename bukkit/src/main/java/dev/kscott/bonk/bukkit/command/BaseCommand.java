package dev.kscott.bonk.bukkit.command;

import cloud.commandframework.CommandManager;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a command.
 */
public interface BaseCommand {

    /**
     * Registers this command with {@code commandManager}.
     *
     * @param commandManager CommandManager to register with
     */
    void register(final @NonNull CommandManager<CommandSender> commandManager);

}
