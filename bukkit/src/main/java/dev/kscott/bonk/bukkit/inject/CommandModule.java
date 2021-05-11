package dev.kscott.bonk.bukkit.inject;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * Provides the command manager.
 */
public final class CommandModule extends AbstractModule {

    /**
     * The command manager.
     */
    private final @MonotonicNonNull PaperCommandManager<CommandSender> commandManager;

    /**
     * Constructs {@code CommandModule}.
     *
     * @param plugin the plugin
     */
    public CommandModule(final @NonNull Plugin plugin) {
        try {
            final @NonNull Function<CommandSender, CommandSender> mapper;
            mapper = Function.identity();

            commandManager = new PaperCommandManager<>(
                    plugin,
                    AsynchronousCommandExecutionCoordinator.<CommandSender>newBuilder().withAsynchronousParsing().build(),
                    mapper,
                    mapper
            );

            if (commandManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the CommandManager", e);
        }
    }

    /**
     * {@return the command manager}
     */
    @Provides
    @Singleton
    public CommandManager<CommandSender> provideCommandManager() {
        return this.commandManager;
    }

    /**
     * {@return the paper command manager}
     */
    @Provides
    @Singleton
    public PaperCommandManager<CommandSender> providePaperCommandManager() {
        return this.commandManager;
    }

}
