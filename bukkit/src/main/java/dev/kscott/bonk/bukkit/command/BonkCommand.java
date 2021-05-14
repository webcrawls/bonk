package dev.kscott.bonk.bukkit.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.player.BonkPlayer;
import dev.kscott.bonk.bukkit.player.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Iterator;

/**
 * The /bonk command.
 */
public final class BonkCommand implements BaseCommand {

    /**
     * The player service.
     */
    private final @NonNull PlayerService playerService;

    /**
     * Constructs {@code BonkCommand}.
     *
     * @param playerService the player service
     */
    @Inject
    public BonkCommand(final @NonNull PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Registers /bonk.
     *
     * @param manager CommandManager to register with
     */
    @Override
    public void register(@NonNull CommandManager<@NonNull CommandSender> manager) {
        final Command.Builder<CommandSender> builder = manager.commandBuilder("bonk");

        manager.command(builder.handler(this::handleBonk));
        manager.command(builder.literal("players")
                .handler(this::handlePlayers));

        manager.command(builder.literal("minigame")
                .argument(StringArgument.of("minigame"))
                .argument(StringArgument.of("operation"))
        );
    }

    /**
     * Handles /bonk.
     *
     * @param ctx command context
     */
    private void handleBonk(final @NonNull CommandContext<CommandSender> ctx) {

    }

    /**
     * Handles /bonk players
     *
     * @param ctx command context
     */
    private void handlePlayers(final @NonNull CommandContext<CommandSender> ctx) {
        final @NonNull CommandSender sender = ctx.getSender();

        final @NonNull Collection<BonkPlayer> players = this.playerService.players();

        if (players.isEmpty()) {
            sender.sendMessage(Component.text("There are no players playing Bonk!"));
            return;
        }

        @NonNull Component playersComponent = Component.empty();

        final @NonNull Iterator<BonkPlayer> pliterator = players.iterator();

        while (pliterator.hasNext()) {
            final @NonNull BonkPlayer player = pliterator.next();

            playersComponent = playersComponent.append(Component.text(player.name()));

            if (pliterator.hasNext()) {
                playersComponent = playersComponent.append(Component.text(", "));
            }
        }

        sender.sendMessage(Component.text("There are currently " + players.size() + ":"));
        sender.sendMessage(playersComponent);
    }
}
