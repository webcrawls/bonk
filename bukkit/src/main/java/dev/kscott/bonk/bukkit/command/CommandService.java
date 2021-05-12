package dev.kscott.bonk.bukkit.command;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Manages commands.
 */
public final class CommandService {

    /**
     * An array of {@link BaseCommand}s to register.
     */
    private static final @NonNull Class<? extends BaseCommand>[] COMMANDS = ArrayHelper.create(

    );

    /**
     * Constructs {@code CommandService}.
     *
     * @param injector the Guice injector
     * @param commandManager the CommandManager
     */
    @Inject
    public CommandService(
            final @NonNull Injector injector,
            final @NonNull CommandManager<CommandSender> commandManager
    ) {
        for (final @NonNull Class<? extends BaseCommand> klazz : COMMANDS) {
            injector.getInstance(klazz).register(commandManager);
        }
    }

}
