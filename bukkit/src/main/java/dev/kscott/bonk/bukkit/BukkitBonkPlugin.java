package dev.kscott.bonk.bukkit;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import dev.kscott.bonk.api.player.BonkPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The class entrypoint for Bonk.
 */
public final class BukkitBonkPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

//    /**
//     * Constructs and returns a new {@link CommandManager}.
//     * @param userService
//     * @return
//     */
//    private @NonNull CommandManager<BonkPlayer> commandManager() {
//        final @NonNull PaperCommandManager<@NonNull BonkPlayer> commandManager = new PaperCommandManager<>(
//                this,
//                AsynchronousCommandExecutionCoordinator.<@NonNull BonkPlayer>newBuilder().withAsynchronousParsing().build(),
//                sender -> {
//
//                }
//        )
//    }
}
